package edplatform.edplat.entities.users;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    /**
     * Loads a user by email and all its courses
     * @param email email of the user to be retrieved
     * @return user object with all its courses
     */
    @QueryHints(value = { @QueryHint(name = "QueryHints.PASS_DISTINCT_THROUGH", value = "false")})
    @Query("select distinct u from User u left join fetch u.courses where u.email = :email")
    Optional<User> findByEmailWithCourses(String email);

    /**
     * Loads a user by id and all its courses
     * @param id id of the user to be retrieved
     * @return user object with all its courses
     */
    @QueryHints(value = { @QueryHint(name = "QueryHints.PASS_DISTINCT_THROUGH", value = "false")})
    @Query("select distinct u from User u left join fetch u.courses where u.id = :id")
    Optional<User> findByIdWithCourses(Long id);

    /**
     * Loads all user by course
     * @param course course of the users to be retrieved
     * @return users list
     */
    @QueryHints(value = { @QueryHint(name = "QueryHints.PASS_DISTINCT_THROUGH", value = "false")})
    @Query("select distinct u from User u join fetch u.courses c where c = :course")
    List<User> findAllWithCourses(Course course);

    /**
     * Loads all user with their authorities by course
     * @param course course of the users to be retrieved
     * @return users list
     */
    @QueryHints(value = { @QueryHint(name = "QueryHints.PASS_DISTINCT_THROUGH", value = "false")})
    @Query("select distinct u from User u left join fetch u.authorities where :course in u.courses")
    List<User> findAllWithAuthorities(Course course);
}
