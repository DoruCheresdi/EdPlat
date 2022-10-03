package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseRepository;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Random;

/**
 * Random data generation class for courses
 */
@SpringBootTest
public class AddTests {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

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
        User user = createRandomUser();

        // create and save courses:
        for (int i = 0; i < numberOfCourses; i++) {
            Course generatedCourse = createRandomCourse();
            courseService.createCourse(user, generatedCourse);
        }
    }

    /**
     * Generates a Course with a random name and description
     * @return generated course
     */
    private Course createRandomCourse() {
        int courseNameSize = 12;
        int courseDescriptionSize = 20;

        Course generatedCourse = new Course();
        generatedCourse.setCourseName(generateRandomName(courseNameSize));
        generatedCourse.setDescription(generateRandomName(courseDescriptionSize));
        generatedCourse.setUsers(new ArrayList<>());

        return generatedCourse;
    }

    /**
     * Generates a random name made from lowercase characters
     * @param nameSize number of letter the name has
     * @return a string containing lowercase letters
     */
    private String generateRandomName(Integer nameSize) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(nameSize)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Creates a random user, encrypts its password and persists it
     * @return user created
     */
    private User createRandomUser() {
        User user = getRandomUser();
        userService.encryptPassword(user);
        userService.save(user);

        return user;
    }

    /**
     * Returns a random user with valid data (email is unique).
     * @return
     */
    private User getRandomUser() {
        User user = new User();

        String email;
        do {
            email = generateRandomName(10) + "@" + generateRandomName(4);
        } while(userService.findByEmail(email).isPresent());
        user.setEmail(email);
        user.setFirstName(generateRandomName(10));
        user.setLastName(generateRandomName(13));
        user.setPassword(generateRandomName(14));
        user.setCourses(new ArrayList<>());

        return user;
    }
}
