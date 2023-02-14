package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.users.User;

import java.util.List;

public interface CourseEnrollRequestService {

    List<CourseEnrollRequest> findAllByUser(User user);
}
