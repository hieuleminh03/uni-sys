package controller.general;

import controller.BaseController;
import dto.request.shared.AuthenticationRequest;
import dto.response.BaseResponse;
import dto.response.shared.AuthenticationResponse;
import dto.request.shared.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Authentication API endpoints")
public class AuthController extends BaseController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user credentials and returns access and refresh tokens")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        return successResponse(authenticationResponse, "Login successful");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Generates a new access token using a valid refresh token")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        AuthenticationResponse refreshedResponse = authenticationService.refreshToken(request);
        return successResponse(refreshedResponse, "Token refreshed successfully");
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Invalidates the user token")
    public ResponseEntity<BaseResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        boolean logoutSuccess = authenticationService.logoutByToken(authHeader);
        return logoutSuccess 
            ? successResponse("Successfully logged out", "Logout successful")
            : unauthorizedResponse("No active session found");
    }
}
