package edplatform.edplat.entities.users;

import edplatform.edplat.entities.courses.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    /**
     * Retrieve a user by its email
     * @param email User's email
     */
    public Optional<User> findByEmail(String email);

    /**
     * Loads a user by email and all its courses
     * @param email email of the user to be retrieved
     * @return user object with all its courses
     */
    Optional<User> findByEmailWithCourses(String email);

    /**
     * Method to return a list of results indicated by the pageable object
     * @param pageable object containing information about the page
     * @return
     */
    public Page<User> findAll(Pageable pageable);

    /**
     * Encrypts a user password
     * @param user User with password in plain to be encrypted
     */
    public void encryptPassword(User user);

    /**
     * Deletes a user and all its submissions.
     * Deletes courses user owns if they don't have any other owners.
     * @param user User to be deleted
     */
    public void deleteUser(User user);

    /**
     * Persists a User
     * @param user User to be persisted
     */
    public void save(User user);
}
