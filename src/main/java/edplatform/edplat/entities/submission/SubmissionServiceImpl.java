package edplatform.edplat.entities.submission;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.utils.FilePathBuilder;
import edplatform.edplat.utils.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

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
        user.getSubmissions().remove(submission);

        String uploadDir = filePathBuilder.getSubmissionFileDirectory(assignment, user);

        try {
            FileUploadUtil.deleteFile(uploadDir, submission.getSubmissionResource());
        } catch (IOException e) {
            log.error("Can't delete submission file");
        }

        submissionRepository.delete(submission);
    }
}
