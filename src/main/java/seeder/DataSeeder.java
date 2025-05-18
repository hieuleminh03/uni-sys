package seeder;

import model.Account;
import model.Admin;
import model.User;
import model.enums.AccountStatus;
import model.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import repository.AccountRepository;
import repository.AdminRepository;
import repository.UserRepository;

@Profile({"dev", "docker"})
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, AccountRepository accountRepository, 
                     AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String encodedPassword = passwordEncoder.encode("123456");
        if (accountRepository.count() == 0 && adminRepository.count() == 0) {
            // Create admin account
            Account account = Account.builder()
                    .username("admin")
                    .password(encodedPassword)
                    .status(AccountStatus.ACTIVE)
                    .build();
            accountRepository.save(account);

            // Create user associated with admin
            User user = User.builder()
                    .account(account)
                    .role(Role.ADMIN)
                    .fullName("Admin User")
                    .email("admin@example.com")
                    .build();
            userRepository.save(user);

            // Create admin entry
            Admin admin = Admin.builder()
                    .user(user)
                    .note("{\"createdBy\": \"system\", \"permissions\": \"full\"}")
                    .build();
            adminRepository.save(admin);
        }
    }

}
