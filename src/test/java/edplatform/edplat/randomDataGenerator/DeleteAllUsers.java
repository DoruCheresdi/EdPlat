package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DeleteAllUsers {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @Rollback(value = false)
    public void deleteAllUsers() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);;
        Page<User> listUsers;

        // delete page by page until there are no more courses:
        do {
            listUsers = userService.findAll(pageable);
            for (User user :
                    listUsers) {
                userService.deleteUser(user);
            }
        } while(listUsers.getTotalElements() > 0);
    }
}
