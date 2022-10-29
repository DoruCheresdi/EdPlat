package edplatform.edplat.entities.assignment;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.submission.Submission;
import edplatform.edplat.entities.users.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AssignmentService {

    public Optional<Assignment> findById(Long id);

    /**
     * Persists an assignment
     * @param assignment
     */
    public void save(Assignment assignment);

    /**
     * Adds a new submission to the assignment, using the multipart file as a resource
     * @param multipartFile file to be used as a resource
     * @param assignmentId id of the assignment to add the submission to
     * @param user user who submits the resource as a submission
     * @throws Exception
     */
    public void addSubmission(MultipartFile multipartFile, Long assignmentId, User user) throws Exception;

    /**
     * Returns all assignments and their submissions of a course
     * @param course course whose assignment must be retrieved
     * @return all assignments with their submissions
     */
    public List<Assignment> findAllAssignmentsWithSubmissionsByCourse(Course course);

    /**
     * Method that returns whether the user has made a submission
     * for the given assignment
     * @param assignment assignment that the user should have made the sumbission
     * @param user user that has made the submission
     * @return whether the user has a submission for the assignment
     */
    public boolean hasUserSubmitted(Assignment assignment, User user);

    /**
     * Returns the submission made by the user to the assignment
     * @param assignment assignment where the submission is sought
     * @param user user whose submission is sought
     * @return submission made by the user for the assignment
     */
    public Submission getSubmissionForUser(Assignment assignment, User user);
}
