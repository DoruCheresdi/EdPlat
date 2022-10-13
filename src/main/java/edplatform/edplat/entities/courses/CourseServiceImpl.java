package edplatform.edplat.entities.courses;

import edplatform.edplat.entities.authority.AuthorityService;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import edplatform.edplat.security.AuthorityStringBuilder;
import edplatform.edplat.security.SecurityAuthorizationChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthorityStringBuilder authorityStringBuilder;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityAuthorizationChecker securityAuthorizationChecker;

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> findByCourseName(String courseName) {
        return courseRepository.findByCourseName(courseName);
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public void save(Course course) {
        // add the time it was added to the course:
        Timestamp courseCreatedAt = new Timestamp(System.currentTimeMillis());
        course.setCreatedAt(courseCreatedAt);

        log.info("Saving course with name {} at timestamp {}",
                course.getCourseName(), courseCreatedAt);

        courseRepository.save(course);
    }

    @Override
    public void createCourse(User user, Course course) {
        // add course to user:
        course.getUsers().add(user);
        // I don't know why I have to do this(course is the owner of the relationship, so adding
        // the course to the user's list shouldn't be necessary):
        user.getCourses().add(course);
        this.save(course);
        // retrieve from DB to get id for authority creation:
        course = courseRepository.findByCourseName(course.getCourseName()).get();

        // add the course owner authority to the user that created the course:
        String authorityName = authorityStringBuilder.getCourseOwnerAuthority(course.getId().toString());
        authorityService.giveAuthorityToUser(user, authorityName);
    }

    @Override
    public void enrollUserToCourse(Course course, User user) {
        if (user.getCourses().contains(course)) {
            log.error("User {} already has course {}", user.getEmail(), course.getCourseName());
            return;
        }
        course.getUsers().add(user);
        this.save(course);

        // add the course owner authority to the user that created the course:
        String authorityName = authorityStringBuilder.getCourseEnrolledAuthority(course.getId().toString());
        authorityService.giveAuthorityToUser(user, authorityName);
    }

    @Override
    public List<User> getOwnerUsers(Course course) {
        return course.getUsers().stream()
                .filter(user -> securityAuthorizationChecker.checkCourseOwner(user, course))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourse(Course course) {
        // delete all authorities regarding course:
        // for owners:
        String ownerAuthority = authorityStringBuilder.getCourseOwnerAuthority(course.getId().toString());
        authorityService.deleteAuthority(ownerAuthority);
        // for enrolled:
        String enrolledAuthority = authorityStringBuilder.getCourseEnrolledAuthority(course.getId().toString());
        authorityService.deleteAuthority(enrolledAuthority);

        // delete all assignments and the course itself (delete is cascading):
        course.setUsers(new ArrayList<>());
        courseRepository.delete(course);
    }
}
