package edplatform.edplat.tests;

import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import edplatform.edplat.entities.users.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldDeleteSimpleUser() {
        User user = new User();
        user.setEmail("userTest@test.com");
        user.setPassword(passwordEncoder.encode("test"));
        user.setCourses(new ArrayList<>());
        user.setSubmissions(new ArrayList<>());

        userRepository.save(user);

        userService.deleteUser(user);

        assertThat(userRepository.findByEmail("userTest@test.com").isPresent())
                .isFalse();

        // clean up since test is not transactional:
        if (userRepository.findByEmail("userTest@test.com").isPresent()) {
            userRepository.delete(user);
        }
    }
}
