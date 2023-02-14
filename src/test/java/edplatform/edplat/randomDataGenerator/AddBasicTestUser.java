package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Component
public class AddBasicTestUser {

    @Autowired
    private UserService userService;

    @Autowired
    private DatabaseOperations databaseOperations;

    @Test
    @Transactional
    @Rollback(value = false)
    public void addBasicTestUser() throws Exception {
        databaseOperations.addBasicTestUser();
    }
}
