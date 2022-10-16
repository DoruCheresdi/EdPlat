package edplatform.edplat.entities.assignment;

import edplatform.edplat.entities.courses.CourseRepository;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.submission.Submission;
import edplatform.edplat.entities.submission.SubmissionService;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.utils.FilePathBuilder;
import edplatform.edplat.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private FilePathBuilder filePathBuilder;

    @Autowired
    private SubmissionService submissionService;

    @Override
    public Optional<Assignment> findById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Override
    public void save(Assignment assignment) {
        assignmentRepository.save(assignment);
    }

    @Override
    public void addSubmission(MultipartFile multipartFile, Long assignmentId, User user) throws Exception {
        Optional<Assignment> optionalAssignment = findById(assignmentId);
        Assignment assignment;
        if (optionalAssignment.isPresent()) {
            assignment = optionalAssignment.get();
        } else {
            throw new Error("Error retrieving course from database");
        }
        // create submission:
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setUser(user);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        submission.setSubmissionResource(fileName);

        // save submission file:
        String uploadDir = filePathBuilder.getSubmissionFileDirectory(assignment, user);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        // save submission to database:
        submissionService.save(submission);
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
