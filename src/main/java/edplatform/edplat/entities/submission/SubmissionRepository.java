package edplatform.edplat.entities.submission;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    Optional<Submission> findSubmissionByAssignmentIdAndAndUserId(Long assignmentId, Long userId);
}
