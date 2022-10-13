package edplatform.edplat.security;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.authority.AuthorityService;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Bean used in the security filter chain to check if users have certain authorities
 */
@Component
public class SecurityAuthorizationChecker {

    @Autowired
    private AuthorityStringBuilder authorityStringBuilder;

    @Autowired
    private AuthorityService authorityService;

    /**
     * check if the authenticated user is the owner of the course given in the request parameter:
     * @param authentication authentication
     * @param httpServletRequest request
     * @return whether the authorities in the authentication match the one needed for the request
     */
    public boolean checkCourseOwnerByCourseId(Authentication authentication, HttpServletRequest httpServletRequest) {
        return checkCourseOwner("courseId", authentication, httpServletRequest);
    }

    /**
     * check if the authenticated user is the owner of the course given in the request parameter:
     * @param authentication authentication
     * @param httpServletRequest request
     * @return whether the authorities in the authentication match the one needed for the request
     */
    public boolean checkCourseOwnerById(Authentication authentication, HttpServletRequest httpServletRequest) {
        return checkCourseOwner("id", authentication, httpServletRequest);
    }

    /**
     * check if the authenticated user is the owner of the course given in the request parameter:
     * @param parameterName name of the parameter from the request where the courseId is
     * @param authentication authentication
     * @param httpServletRequest
     * @return whether the authorities in the authentication match the one needed for the request
     */
    private boolean checkCourseOwner(String parameterName, Authentication authentication, HttpServletRequest httpServletRequest) {
        String courseId = httpServletRequest.getParameter(parameterName);
        String courseOwnerAuthority = authorityStringBuilder.getCourseOwnerAuthority(courseId);
        // check if there is any authority matching this course:
        var authority = authentication.getAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals(courseOwnerAuthority))
                .findFirst();
        return authority.isPresent();
    }

    /**
     * Checks if a user is a owner of a course
     * @param user user to be checked
     * @param course course whose owner the user must be
     * @return whether the user is an owner of the course
     */
    public boolean checkCourseOwner(User user, Course course) {
        String courseOwnerAuthorityName = authorityStringBuilder.getCourseOwnerAuthority(course.getId().toString());
        Authority authority = authorityService.findByName(courseOwnerAuthorityName).get();
        return user.getAuthorities().contains(authority);
    }

    /**
     * Checks if a user is enrolled in a course
     * @param user user to be checked
     * @param course course where the user is verified to be enrolled
     * @return whether the user is enrolled in the course
     */
    public boolean checkCourseEnrolled(User user, Course course) {
        String courseOwnerAuthorityName = authorityStringBuilder.getCourseEnrolledAuthority(course.getId().toString());
        Authority authority = authorityService.findByName(courseOwnerAuthorityName).get();
        return user.getAuthorities().contains(authority);
    }
}
