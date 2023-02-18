package edplatform.edplat.repositories;

import edplatform.edplat.entities.submission.Submission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    @QueryHints(value = { @QueryHint(name = "QueryHints.PASS_DISTINCT_THROUGH", value = "false")})
    @Query("select distinct s from Submission s left join fetch s.user where s.assignment.id = :assignmentId")
    List<Submission> findAllByAssignmentIdWithUser(Long assignmentId);
}
