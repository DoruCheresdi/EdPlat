package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

/**
 * Random data generation class for courses
 */
@SpringBootTest
public class AddTests {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void add10Courses() {
        addCourses(10);
    }

    /**
     * Creates and saves a number of courses to the database
     * @param numberOfCourses number of courses to be added
     */
    public void addCourses(Integer numberOfCourses) {
        // create and save courses:
        for (int i = 0; i < numberOfCourses; i++) {
            Course generatedCourse = createRandomCourse();
            courseRepository.save(generatedCourse);
        }
    }

    /**
     * Generates a Course with a random name and description
     * @return generated course
     */
    public Course createRandomCourse() {
        int courseNameSize = 12;
        int courseDescriptionSize = 20;

        Course generatedCourse = new Course();
        generatedCourse.setCourseName(generateRandomName(courseNameSize));
        generatedCourse.setDescription(generateRandomName(courseDescriptionSize));

        return generatedCourse;
    }

    /**
     * Generates a random name made from lowercase characters
     * @param nameSize number of letter the name has
     * @return a string containing lowercase letters
     */
    public String generateRandomName(Integer nameSize) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(nameSize)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
