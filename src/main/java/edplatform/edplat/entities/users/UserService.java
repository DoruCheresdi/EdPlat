package edplatform.edplat.entities.users;

import edplatform.edplat.entities.courses.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    /**
     * Retrieve a user by its email
     * @param email user's email
     */
    public User findByEmail(String email);

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

    public void save(User user);
}
