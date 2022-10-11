package edplatform.edplat.entities.assignment;

import edplatform.edplat.entities.submission.Submission;
import edplatform.edplat.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public Optional<Assignment> findById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Override
    public void save(Assignment assignment) {
        assignmentRepository.save(assignment);
    }

    @Override
    @Transactional
    public boolean hasUserSubmitted(Assignment assignment, User user) {
        return assignment.getSubmissions().stream()
                .anyMatch(submission -> submission.getUser().equals(user));
    }

    @Override
    @Transactional
    public Submission getSubmissionForUser(Assignment assignment, User user) {
        return assignment.getSubmissions().stream()
                .filter(submission -> submission.getUser().equals(user))
                .findAny().get();
    }
}
