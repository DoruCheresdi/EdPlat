package edplatform.edplat.entities.users;

import edplatform.edplat.entities.courses.Course;

public interface UserService {

    /**
     * Retrieve a user by its email
     * @param email user's email
     */
    public User findByEmail(String email);

    public void save(User user);
}
