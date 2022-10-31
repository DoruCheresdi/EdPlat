package edplatform.edplat.entities.users;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.authority.AuthorityRepository;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
    @Transactional
    public void deleteUser(User user) {
        for (Course course :
                user.getCourses()) {
            // delete course if the user deleted is the only owner of the course:
            if(courseService.getOwnerUsers(course).size() <= 1) {
                courseService.deleteCourse(course);
            }
        }

        user.setAuthorities(new HashSet<>(authorityRepository.findAllByUserEmail(user.getEmail())));

        for (Authority authority :
                user.getAuthorities()) {
            authority.getUsers().remove(user);
        }

        userRepository.delete(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
