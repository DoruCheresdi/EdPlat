package edplatform.edplat.entities.submission;

import edplatform.edplat.entities.assignment.Assignment;

import java.util.List;

public interface SubmissionService {

    public void save(Submission submission);

    /**
     * Deletes a submission. Also delete the submission file from disk
     * @param submission submission to be deleted
     */
    public void delete(Submission submission);

    public List<Submission> findAllByAssignmentIdWithUser(Long assignmentId);

    public void updateGrade(Long submissionId, Float grade);

    public Assignment getAssignmentForSubmissionId(Long submissionId);
}
