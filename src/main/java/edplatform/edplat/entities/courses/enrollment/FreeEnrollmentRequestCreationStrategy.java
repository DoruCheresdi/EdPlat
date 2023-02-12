package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.controllers.controllerUtils.AuthenticationUpdater;
import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.authority.AuthorityService;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.security.AuthorityStringBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreeEnrollmentRequestCreationStrategy implements EnrollmentRequestCreationStrategy {

    private final AuthenticationUpdater authenticationUpdater;

    private final AuthorityStringBuilder authorityStringBuilder;

    private final AuthorityService authorityService;

    @Transactional
    public void enroll(User user, Course course, Authentication authentication) {
        // enroll user in course without asking permission:
        course.getUsers().add(user);

        // add the course enrolled authority to the user that enrolled in the course:
        String authorityName = authorityStringBuilder.getCourseEnrolledAuthority(course.getId().toString());
        authorityService.giveAuthorityToUser(user, authorityName);

        // update authorities on the authenticated user:
        Authority authority = new Authority(
                authorityStringBuilder.getCourseEnrolledAuthority(course.getId().toString()));
        authenticationUpdater.addAuthorityToAuthentication(authority, authentication);
    }
}
