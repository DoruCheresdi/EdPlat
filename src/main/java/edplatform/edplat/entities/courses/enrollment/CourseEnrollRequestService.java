package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;

import java.util.List;

public interface CourseEnrollRequestService {

    List<CourseEnrollRequest> findAllByUser(User user);

    List<CourseEnrollRequest> findAllByCourse(Course course);

    /**
     * Accepts a request by deleting it from the database and enrolling the user in the course
     * @param courseId
     * @param userId
     */
    void acceptRequest(Long courseId, Long userId);
}
