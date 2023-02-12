package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.courses.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import edplatform.edplat.entities.users.User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OwnerDecidesEnrollmentRequestCreationStrategy implements EnrollmentRequestCreationStrategy {

    public void enroll(User user, Course course, Authentication authentication) {
        // create join request:
        CourseJoinRequest courseJoinRequest = new CourseJoinRequest();
        courseJoinRequest.setCourse(course);
        courseJoinRequest.setUser(user);
        // add join request to the course's list of requests:
        course.getJoinRequests().add(courseJoinRequest);
    }
}
