package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.courses.Course;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import edplatform.edplat.entities.users.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerDecidesEnrollmentRequestCreationStrategy implements EnrollmentRequestCreationStrategy {

    public void enroll(User user, Course course, Authentication authentication) {
        // create join request:
        CourseEnrollRequest courseEnrollRequest = new CourseEnrollRequest();
        courseEnrollRequest.setCourse(course);
        courseEnrollRequest.setUser(user);
        Timestamp requestCreatedAt = new Timestamp(System.currentTimeMillis());
        courseEnrollRequest.setCreatedAt(requestCreatedAt);

        // add join request to the course's list of requests:
        course.getEnrollRequests().add(courseEnrollRequest);

        log.info("Created course enroll request for user with id {} in course with id {} at {}",
                user.getId(), course.getId(), courseEnrollRequest.getCreatedAt());
    }
}
