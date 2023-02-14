package edplatform.edplat.randomDataGenerator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Component
public class DeleteDatabaseAndAddTestDataWithUser {

    @Autowired
    private DatabaseOperations databaseOperations;

    @Test
    @Transactional
    @Rollback(value = false)
    public void resetDatabase() throws Exception {
        databaseOperations.deleteAllUsers();
        databaseOperations.addCoursesWithAssignments(10);
        databaseOperations.addBasicTestUser();
    }
}
