package edplatform.edplat;

import edplatform.edplat.controllers.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EdPlatApplicationTests {

    @Autowired
    private UserController userController;

    // sanity check test:
    @Test
    void contextLoads() {
        assertThat(userController).isNotNull();
    }

}
