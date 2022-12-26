package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DeleteAllTables {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void deleteAllTables() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
        assignmentRepository.deleteAll();
        submissionRepository.deleteAll();
        authorityRepository.deleteAll();
    }
}
