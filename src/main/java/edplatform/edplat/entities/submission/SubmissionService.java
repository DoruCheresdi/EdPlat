package edplatform.edplat.entities.submission;

public interface SubmissionService {

    public void save(Submission submission);

    /**
     * Deletes a submission. Also delete the submission file from disk
     * @param submission submission to be deleted
     */
    public void delete(Submission submission);
}
