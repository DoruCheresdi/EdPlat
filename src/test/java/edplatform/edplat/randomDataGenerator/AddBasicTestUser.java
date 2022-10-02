package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
public class AddBasicTestUser {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @Rollback(value = false)
    public void addBasicTestUser() throws Exception {
        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setPassword("XA9!jMti5x");

        userService.encryptPassword(user);
        if (userService.findByEmail("test@test.com").isPresent()) {
            throw new Exception("User already exists");
        } else {
            userService.save(user);
        }
    }
}
