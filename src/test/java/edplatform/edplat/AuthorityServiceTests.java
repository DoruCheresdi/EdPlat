package edplatform.edplat;

import edplatform.edplat.entities.users.User;
import edplatform.edplat.security.AuthorityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthorityServiceTests {

    @Autowired
    private AuthorityService authorityService;

    @Test
    void shouldGiveRightAuthorityToUser() {
    }
}
