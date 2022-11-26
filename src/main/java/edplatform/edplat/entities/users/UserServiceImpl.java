package edplatform.edplat.entities.users;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.authority.AuthorityRepository;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseRepository;
import edplatform.edplat.entities.courses.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailWithCourses(String email) {
        return userRepository.findByEmailWithCourses(email);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void encryptPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    @Override
    public void deleteUser(User user) {
        user.setCourses(courseRepository.findAllByUser(user));

        // delete the courses that will have no owner after user deletion:
        List<Course> coursesForDeletion = new ArrayList<>();
        for (Course course :
                user.getCourses()) {
            // delete course if the user deleted is the only owner of the course:
            if(courseService.getOwnerUsers(course).size() <= 1) {
                coursesForDeletion.add(course);
            }
        }
        for (Course course :
                coursesForDeletion) {
            courseService.deleteCourse(course);
        }

        // sinchronize entity before deletion:
        user = userRepository.findByEmail(user.getEmail()).get();
        userRepository.delete(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
