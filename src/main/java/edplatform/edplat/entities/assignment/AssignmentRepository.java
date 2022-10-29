package edplatform.edplat.entities.assignment;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.submission.Submission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends CrudRepository<Assignment, Long> {

    Optional<Assignment> findByAssignmentName(String assignmentName);

    @QueryHints(value = { @QueryHint(name = "QueryHints.PASS_DISTINCT_THROUGH", value = "false")})
    @Query("select distinct a from Assignment a left join fetch a.submissions where a.course = :course")
    List<Assignment> findAllAssignmentsWithSubmissionsByCourse(Course course);
}
