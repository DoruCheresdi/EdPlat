package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollment;
import edplatform.edplat.entities.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Component
public class AddCoursesWithAssignments {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DataGenerator dataGenerator;

    @Autowired
    private DatabaseOperations databaseOperations;

    @Test
    @Transactional
    @Rollback(value = false)
    public void addCoursesWithAssignments() {
        databaseOperations.addCoursesWithAssignments(10);
    }

}
