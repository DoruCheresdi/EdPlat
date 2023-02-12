package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.courses.Course;
import org.springframework.security.core.Authentication;
import edplatform.edplat.entities.users.User;

public interface EnrollmentRequestCreationStrategy {

    public void enroll(User user, Course course, Authentication authentication);
}
