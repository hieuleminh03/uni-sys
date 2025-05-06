package seeder;

import model.Account;
import model.User;
import model.enums.AccountStatus;
import model.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import repository.AccountRepository;
import repository.UserRepository;

@Profile("dev")
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String encodedPassword = passwordEncoder.encode("123456");
        if (userRepository.count() == 0) {
            Account account = Account.builder()
                    .username("admin")
                    .password(encodedPassword)
                    .status(AccountStatus.ACTIVE)
                    .build();
            accountRepository.save(account);

            User user = User.builder()
                    .account(account)
                    .role(Role.ADMIN)
                    .fullName("Admin User")
                    .email("admin@example.com")
                    .build();
            userRepository.save(user);
        }
    }

}
