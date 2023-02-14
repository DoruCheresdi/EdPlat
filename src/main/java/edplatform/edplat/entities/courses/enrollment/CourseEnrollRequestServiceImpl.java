package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.repositories.CourseEnrollRequestRepository;
import edplatform.edplat.repositories.CourseRepository;
import edplatform.edplat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseEnrollRequestServiceImpl implements CourseEnrollRequestService {

    @Autowired
    private CourseEnrollRequestRepository courseEnrollRequestRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FreeEnrollmentRequestCreationStrategy freeEnrollmentRequestCreationStrategy;

    @Override
    public List<CourseEnrollRequest> findAllByUser(User user) {
        return courseEnrollRequestRepository.findAllByUser(user);
    }

    @Override
    public List<CourseEnrollRequest> findAllByCourse(Course course) {
        return courseEnrollRequestRepository.findAllByCourse(course);
    }

    @Override
    @Transactional
    public void acceptRequest(Long courseId, Long userId) {
        // delete request:
        Course course = courseRepository.findById(courseId).get();
        User user = userRepository.findById(userId).get();
        CourseEnrollRequest courseEnrollRequest = courseEnrollRequestRepository.findByCourseAndUser(course, user).get();
        courseEnrollRequestRepository.delete(courseEnrollRequest);

        // enroll user to course:
        freeEnrollmentRequestCreationStrategy.enroll(user, course);
    }
}
