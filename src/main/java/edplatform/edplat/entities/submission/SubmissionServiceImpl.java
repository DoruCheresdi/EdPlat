package edplatform.edplat.entities.submission;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.repositories.SubmissionRepository;
import edplatform.edplat.utils.FilePathBuilder;
import edplatform.edplat.utils.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FilePathBuilder filePathBuilder;

    @Override
    public void save(Submission submission) {
        submissionRepository.save(submission);
    }

    @Override
    @Transactional
    public void delete(Submission submission) {
        Assignment assignment = submission.getAssignment();
        assignment.getSubmissions().remove(submission);

        User user = submission.getUser();

        String uploadDir = filePathBuilder.getSubmissionFileDirectory(assignment, user);

        // delete file from disk:
        try {
            FileUploadUtil.deleteFile(uploadDir, submission.getSubmissionResource());
        } catch (IOException e) {
            log.error("Can't delete submission file in upload directory {}", uploadDir);
        }

        // delete folder for submission:
        try {
            FileUtils.deleteDirectory(new File(uploadDir));
        } catch (IOException e) {
            log.error("Can't delete submission folder file {}", uploadDir);
        }

        submissionRepository.delete(submission);
    }

    public List<Submission> findAllByAssignmentIdWithUser(Long assignmentId) {
        return submissionRepository.findAllByAssignmentIdWithUser(assignmentId);
    }

    public void updateGrade(Long submissionId, Float grade) {
        Submission submission = submissionRepository.findById(submissionId).get();
        submission.setGraded(true);
        submission.setGrade(grade);
        submissionRepository.save(submission);
        log.info("Added grade {} to submission with id {}", grade, submissionId);
    }

    public Assignment getAssignmentForSubmissionId(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId).get();
        return submission.getAssignment();
    }
}
