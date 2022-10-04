package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

@Component
public class DataGenerator {

    @Autowired
    private UserService userService;

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
            email = generateRandomName(10) + "@" + generateRandomName(4);
        } while(userService.findByEmail(email).isPresent());
        user.setEmail(email);
        user.setFirstName(generateRandomName(10));
        user.setLastName(generateRandomName(13));
        user.setPassword(generateRandomName(14));
        user.setCourses(new ArrayList<>());

        return user;
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
        generatedCourse.setUsers(new ArrayList<>());

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
