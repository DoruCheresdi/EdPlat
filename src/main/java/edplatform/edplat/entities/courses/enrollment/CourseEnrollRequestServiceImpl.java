package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.users.User;
import edplatform.edplat.repositories.CourseEnrollRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseEnrollRequestServiceImpl implements CourseEnrollRequestService {

    @Autowired
    private CourseEnrollRequestRepository courseEnrollRequestRepository;

    @Override
    public List<CourseEnrollRequest> findAllByUser(User user) {
        return courseEnrollRequestRepository.findAllByUser(user);
    }
}
