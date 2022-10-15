package edplatform.edplat.randomDataGenerator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AddUsers {

    @Autowired
    private DataGenerator dataGenerator;

    @Test
    public void addUsers() {
        int numberOfUsersToAdd = 20;

        for (int i = 0; i < numberOfUsersToAdd; i++) {
            dataGenerator.createRandomUser();
        }
    }
}
