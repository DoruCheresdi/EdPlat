package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import org.hibernate.dialect.Database;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Component
public class DeleteAllUsers {

    @Autowired
    private UserService userService;

    @Autowired
    private DatabaseOperations databaseOperations;

    @Test
    @Transactional
    @Rollback(value = false)
    public void deleteAllUsers() {
        databaseOperations.deleteAllUsers();
    }
}
