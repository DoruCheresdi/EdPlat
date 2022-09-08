package edplatform.edplat;

import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import edplatform.edplat.security.AuthorityService;
import edplatform.edplat.security.AuthorityStringBuilder;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AuthorityServiceTests {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityStringBuilder authorityStringBuilder;

    @Test
    @Transactional
    void shouldGiveRightAuthorityToUser() {
        User user = new User();
        String email = "authorityTest@test.com";
        String testFirstName = "testFirstName";
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("passTest"));
        user.setFirstName(testFirstName);
        userRepository.save(user);

        // get user from database and make sure it is the right one:
        User foundUser = userRepository.findByEmail(email);
        Hibernate.initialize(foundUser.getAuthorities());
        foundUser.setAuthorities(new ArrayList<>());
        assertThat(foundUser.getFirstName()).isEqualTo(testFirstName);

        authorityService.giveAuthorityToUser(foundUser, authorityStringBuilder.getCourseOwnerAuthority("4"));

        // retrieve the persisted user again:
        foundUser = userRepository.findByEmail(email);
        assertThat(foundUser.getAuthorities().size()).isEqualTo(1);
        assertThat(foundUser.getAuthorities().get(0).getAuthority())
                .isEqualTo(authorityStringBuilder.getCourseOwnerAuthority("4"));
    }
}
