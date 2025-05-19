package service;

import dto.request.shared.AuthenticationRequest;
import dto.request.shared.RefreshTokenRequest;
import dto.response.shared.AuthenticationResponse;
import exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import model.Token;
import model.User;
import model.enums.AccountStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AccountRepository;
import repository.TokenRepository;
import repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticate a user and return JWT tokens
     * @param request authentication request with username and password
     * @return AuthenticationResponse containing access and refresh tokens
     * @throws AuthenticationException if authentication fails
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Authenticate with Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get user details after successful authentication
            Account account = accountRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AuthenticationException("Account not found with username: " + request.getUsername()));

            if (account.getStatus() == AccountStatus.BANNED) {
                throw new AuthenticationException("Account is banned");
            }

            User user = userRepository.findByAccount(account)
                    .orElseThrow(() -> new AuthenticationException("Can't find user information, try again later"));

            // Generate tokens
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // Save the token
            saveUserToken(user, accessToken);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .role(user.getRole())
                    .build();

        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid username or password");
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed", e);
        }
    }

    /**
     * Save user token
     */
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    /**
     * Revoke all tokens for user
     */
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
        // Log the number of tokens revoked for debugging
        log.info("Revoked {} tokens for user ID: {}", validUserTokens.size(), user.getId());
    }

    /**
     * Refresh a JWT token
     * @param request refresh token request containing the refresh token
     * @return new access and refresh tokens
     */
    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        final String refreshToken = request.getRefreshToken();
        final String username = jwtService.extractUsername(refreshToken);
        
        if (username == null) {
            throw new AuthenticationException("Invalid refresh token");
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Account not found with username: " + username));

        if (account.getStatus() == AccountStatus.BANNED) {
            throw new AuthenticationException("Account is banned");
        }

        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new AuthenticationException("Can't find user information, try again later"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new AuthenticationException("Invalid or expired refresh token");
        }

        // Generate new tokens
        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Update user's tokens - revoke old ones
        revokeAllUserTokens(user);
        // Save new token
        saveUserToken(user, accessToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
    
    /**
     * Logout a user by directly invalidating the token from the request
     * This is a more direct approach than trying to find the user from the security context
     * 
     * @param authHeader the Authorization header from the request, containing the JWT token
     * @return true if the logout was successful, false otherwise
     */
    @Transactional
    public boolean logoutByToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Logout failed: Invalid or missing Authorization header");
            return false;
        }
        
        try {
            // Extract the token from the Authorization header
            String token = authHeader.substring(7);
            log.debug("Processing logout for token: {}", token);
            
            // Get token info from repository (optional, for detailed logging)
            Optional<Token> tokenInfo = tokenRepository.findByToken(token);
            
            if (tokenInfo.isPresent()) {
                Token tokenEntity = tokenInfo.get();
                
                // If token is already invalidated, return success
                if (tokenEntity.isExpired() && tokenEntity.isRevoked()) {
                    log.info("Token was already invalidated");
                    return true;
                }
                
                // Get user for logging purposes
                User user = tokenEntity.getUser();
                log.info("Found token for user: {}", user.getUsername());
                
                // Update the token status directly
                tokenEntity.setExpired(true);
                tokenEntity.setRevoked(true);
                tokenRepository.save(tokenEntity);
                
                log.info("Successfully invalidated token for user: {}", user.getUsername());
            } else {
                // Use the direct revoke method to ensure we catch tokens that might not be
                // properly connected to a user (fallback mechanism)
                int affected = tokenRepository.revokeToken(token);
                log.info("Token invalidation affected {} records", affected);
                
                if (affected == 0) {
                    log.warn("Token not found in database");
                    return false;
                }
            }
            
            // Clear security context
            SecurityContextHolder.clearContext();
            return true;
        } catch (Exception e) {
            log.error("Error during token invalidation", e);
            return false;
        }
    }
    
    /**
     * Original logout method that uses the security context
     * Kept for backward compatibility
     */
    @Transactional
    public String logout() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                log.warn("Logout failed: No authentication found in SecurityContext");
                return null;
            }
            
            if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
                log.warn("Logout failed: Authentication principal is not a UserDetails instance");
                return null;
            }
            
            String username = userDetails.getUsername();
            log.debug("Processing logout for username: {}", username);
            
            // Find the account first, then use it to find the user
            Account account = accountRepository.findByUsername(username)
                    .orElse(null);
                    
            if (account == null) {
                log.warn("Logout failed: Account not found for username: {}", username);
                return null;
            }
            
            User user = userRepository.findByAccount(account)
                    .orElse(null);
            
            if (user == null) {
                log.warn("Logout failed: User not found for account: {}", account.getId());
                return null;
            }
            
            // Revoke all valid tokens for this user
            revokeAllUserTokens(user);
            log.info("Logged out user: {}", username);
            
            // Clear the SecurityContext
            SecurityContextHolder.clearContext();
            return username;
        } catch (Exception e) {
            log.error("Unexpected error during logout", e);
            return null;
        }
    }
}
