package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollment;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class DatabaseOperations {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DataGenerator dataGenerator;


    /**
     * Creates and saves a number of courses to the database
     * @param numberOfCourses number of courses to be added
     */
    public void addCoursesWithAssignments(Integer numberOfCourses) {
        User user = dataGenerator.createRandomUser();

        CourseEnrollment.EnrollmentType enrollmentType = CourseEnrollment.EnrollmentType.FREE;

        // create and save courses:
        for (int i = 0; i < numberOfCourses; i++) {
            // switch enrollment type for diversity:
            if (enrollmentType.equals(CourseEnrollment.EnrollmentType.FREE)) {
                enrollmentType = CourseEnrollment.EnrollmentType.ONE_OWNER_DECIDES;
            } else {
                enrollmentType = CourseEnrollment.EnrollmentType.FREE;
            }

            Course generatedCourse = dataGenerator.createRandomCourse(enrollmentType);
            courseService.createCourse(user.getId(), generatedCourse);

            Course retrievedCourse = courseService
                    .findByCourseName(generatedCourse.getCourseName()).get();

            dataGenerator.giveRandomAssignmentsToCourse(retrievedCourse);
            courseService.save(retrievedCourse);
        }
    }

    /**
     * Adds a basic test user
     * @throws Exception
     */
    public void addBasicTestUser() throws Exception {
        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setPassword("XA9!jMti5x");

        userService.encryptPassword(user);
        if (userService.findByEmail("test@test.com").isPresent()) {
            throw new Exception("User already exists");
        } else {
            userService.save(user);
        }
    }

    /**
     * Deletes all users and all related entities by cascading
     */
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
