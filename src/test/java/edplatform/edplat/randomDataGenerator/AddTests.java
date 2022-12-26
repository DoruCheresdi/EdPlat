package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Random data generation class for courses
 */
@SpringBootTest
public class AddTests {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DataGenerator dataGenerator;

    @Test
    @Transactional
    @Rollback(value = false)
    public void addCourses() {
        addCourses(10);
    }

    /**
     * Creates and saves a number of courses to the database
     * @param numberOfCourses number of courses to be added
     */
    public void addCourses(Integer numberOfCourses) {
        User user = dataGenerator.createRandomUser();

        // create and save courses:
        for (int i = 0; i < numberOfCourses; i++) {
            Course generatedCourse = dataGenerator.createRandomCourse();
            courseService.createCourse(user.getId(), generatedCourse);
        }
    }
}
