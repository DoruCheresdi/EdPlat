package edplatform.edplat.tests.unitTests;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.assignment.AssignmentService;
import edplatform.edplat.entities.assignment.AssignmentServiceImpl;
import edplatform.edplat.entities.submission.Submission;
import edplatform.edplat.entities.users.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class AssignmentServiceImplUnitTests {

    private final AssignmentService assignmentService = new AssignmentServiceImpl();

    @Test
    void shouldFilterSubmissionsCorrectly() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("fsd");
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("fsd");
        User user3 = new User();
        user3.setId(1L);
        user3.setEmail("fsd");
        Assignment assignment = new Assignment();

        Submission submission1 = new Submission();
        submission1.setUser(user1);

        Submission submission2 = new Submission();
        submission2.setUser(user2);

        assignment.setSubmissions(List.of(submission1, submission2));
        assertThat(assignmentService.hasUserSubmitted(assignment, user1));
        assertThat(assignmentService.hasUserSubmitted(assignment, user2));
        assertThat(!assignmentService.hasUserSubmitted(assignment, user3));
    }
}
