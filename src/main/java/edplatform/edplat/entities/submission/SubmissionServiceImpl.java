package edplatform.edplat.entities.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Override
    public void save(Submission submission) {
        submissionRepository.save(submission);
    }
}
