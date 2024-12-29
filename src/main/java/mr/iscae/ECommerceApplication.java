package mr.iscae;

import mr.iscae.entities.User;
import mr.iscae.constants.Role;
import mr.iscae.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class ECommerceApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            if (!userRepository.existsByEmail("zeinebou.dabo@iscae.mr")) {
                User adminRequest = User.builder()
                        .fullName("Zeinebou Dabo")
                        .email("zeinebou.dabo@iscae.mr")
                        .password("12345678")
                        .phone("32447377")
                        .role(Role.ADMIN)
                        .build();


                var adminUser = User.builder()
                        .fullName(adminRequest.getFullName())
                        .phone(adminRequest.getPhone())
                        .email(adminRequest.getEmail())
                        .password(passwordEncoder.encode(adminRequest.getPassword()))
                        .role(adminRequest.getRole())
                        .build();

                userRepository.save(adminUser);
                System.out.println("Default admin user created: " + adminRequest.getEmail());
            }
        };
    }
}
