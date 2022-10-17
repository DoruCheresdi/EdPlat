package edplatform.edplat.entities.courses;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    /**
     * Retrieved a course from db by id
     * @param id id of the course to be retrieved
     */
    public Optional<Course> findById(Long id);

    /**
     * Retrieved a course from db by courseName
     * @param courseName CourseName of the course to be retrieved
     */
    public Optional<Course> findByCourseName(String courseName);

    /**
     * Method to return a list of results indicated by the pageable object
     * @param pageable object containing information about the page
     * @return
     */
    public Page<Course> findAll(Pageable pageable);

    /**
     * Saves a course entity to the DB, course should have a name
     * @param course entity to be saved
     */
    public void save(Course course);

    /**
     * Adds an assignment to a course and persists the entities
     * @param assignment
     * @param courseId
     */
    public void addAssignmentToCourse(Assignment assignment, Long courseId) throws Exception;

    /**
     * Creates course and adds the user as the course's owner. Also creates and adds
     * the corresponding authority to the user
     * @param user user to be given a course
     * @param course course to be added to the user
     */
    public void createCourse(User user, Course course);

    /**
     * Method to add a user to a courses' list. Also adds the enrolled authority to the user
     * @param course course to be given the user
     * @param user user to be given the course
     */
    public void enrollUserToCourse(Course course, User user);

    /**
     * Returns all the owners of the course
     * @param course Course whose owners are sought
     * @return A list of all owner Users
     */
    public List<User> getOwnerUsers(Course course);

    /**
     * Deletes a course and all its authorities, assignments and submissions
     * @param course course to be deleted
     */
    public void deleteCourse(Course course);

    /**
     * Returns all courses which name contains the string provided
     * @param contains string used to search course names
     * @param pageable specifies the page to return
     * @return A list containing courses matching the search and the pagination
     */
    Page<Course> findAllByCourseNameContains(String contains, Pageable pageable);
}
