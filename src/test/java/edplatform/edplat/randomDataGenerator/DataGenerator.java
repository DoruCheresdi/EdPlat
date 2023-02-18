package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollment;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

@Component
public class DataGenerator {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    /**
     * Creates a random user, encrypts its password and persists it
     * @return user created
     */
    public User createRandomUser() {
        User user = getRandomUser();
        userService.encryptPassword(user);
        userService.save(user);

        return user;
    }

    /**
     * Returns a random user with valid data (email is unique).
     * @return
     */
    public User getRandomUser() {
        User user = new User();

        String email;
        do {
            email = generateRandomName(10) + "@" + generateRandomName(4) + ".com";
        } while(userService.findByEmail(email).isPresent());
        user.setEmail(email);
        user.setFirstName(generateRandomName(10));
        user.setLastName(generateRandomName(13));
        // set simple to be able to log in as user:
        user.setPassword("test");
        user.setCourses(new ArrayList<>());

        return user;
    }

    /**
     * Creates and persist a random course, using the given user as owner
     * @param user User to be used as owner
     * @return Created coursed retrieved from the database
     */
    public Course createAndPersistRandomCourseWithUser(User user) {
        Course generatedCourse = createRandomCourse(CourseEnrollment.EnrollmentType.FREE);
        courseService.createCourse(user.getId(), generatedCourse);
        return courseService.findByCourseName(generatedCourse.getCourseName()).get();
    }

    /**
     * Generates a Course with a random name and description
     * @return generated course
     */
    public Course createRandomCourse(CourseEnrollment.EnrollmentType enrollmentType) {
        int courseNameSize = 12;
        int courseDescriptionSize = 20;

        Course generatedCourse = new Course();
        generatedCourse.setCourseName(generateRandomName(courseNameSize));
        generatedCourse.setDescription(generateRandomName(courseDescriptionSize));
        generatedCourse.setUsers(new ArrayList<>());
        generatedCourse.setAssignments(new ArrayList<>());
        generatedCourse.setEnrollmentType(enrollmentType);

        return generatedCourse;
    }

    /**
     * Gives random assignments to course
     * @param course
     */
    public void giveRandomAssignmentsToCourse(Course course) {
        int numberAssignments = 10;

        for (int i = 0; i < numberAssignments; i++) {
            Assignment assignment = createRandomAssignment();
            assignment.setCourse(course);
            course.getAssignments().add(assignment);
        }
    }

    /**
     * Creates a random assignment
     * @return generated assignment
     */
    public Assignment createRandomAssignment() {
        int nameSize = 10;

        Assignment assignment = new Assignment();
        assignment.setAssignmentName(generateRandomName(nameSize));
        assignment.setDescription(generateRandomName(nameSize));

        return assignment;
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
