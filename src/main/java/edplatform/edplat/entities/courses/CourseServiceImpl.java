package edplatform.edplat.entities.courses;

import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import edplatform.edplat.security.AuthorityService;
import edplatform.edplat.security.AuthorityStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Timestamp;
import java.util.Optional;

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
    private UserService userService;

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
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
        user.getCourses().add(course);
        userService.save(user);

        // add the course owner authority to the user that created the course:
        String authorityName = authorityStringBuilder.getCourseOwnerAuthority(course.getId().toString());
        authorityService.giveAuthorityToUser(user, authorityName);

        this.save(course);
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
    public void deleteCourse(Course course) {
        // delete all authorities regarding course:
        // for owners:
        String ownerAuthority = authorityStringBuilder.getCourseOwnerAuthority(course.getId().toString());
        authorityService.deleteAuthority(ownerAuthority);
        // for enrolled:
        String enrolledAuthority = authorityStringBuilder.getCourseEnrolledAuthority(course.getId().toString());
        authorityService.deleteAuthority(enrolledAuthority);

        // delete all assignments and the course itself (delete is cascading):
        for (User user : course.getUsers()) {
            course.getUsers().remove(user);
        }
        courseRepository.delete(course);
    }
}
