package edplatform.edplat.repositories;

import edplatform.edplat.entities.submission.Submission;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    Optional<Submission> findSubmissionByAssignmentIdAndAndUserId(Long assignmentId, Long userId);
}
