package edplatform.edplat.security;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.assignment.AssignmentService;
import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.authority.AuthorityService;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Bean used in the security filter chain to check if users have certain authorities
 */
@Component
@Slf4j
public class SecurityAuthorizationChecker {

    @Autowired
    private AuthorityStringBuilder authorityStringBuilder;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AssignmentService  assignmentService;

    /**
     * Check if the authenticated user is the owner of the course given in the request parameter:
     * @param authentication authentication
     * @param httpServletRequest request
     * @return whether the authorities in the authentication match the one needed for the request
     */
    public boolean checkCourseOwnerByCourseId(Authentication authentication, HttpServletRequest httpServletRequest) {
        return checkCourseOwner("courseId", authentication, httpServletRequest);
    }

    /**
     * Check if the authenticated user is the owner of the course given in the request parameter:
     * @param authentication authentication
     * @param httpServletRequest request
     * @return whether the authorities in the authentication match the one needed for the request
     */
    public boolean checkCourseOwnerById(Authentication authentication, HttpServletRequest httpServletRequest) {
        return checkCourseOwner("id", authentication, httpServletRequest);
    }

    /**
     * Check if the authenticated user is the owner of the course given
     * with the assignment with id given in the request parameter:
     * @param authentication authentication
     * @param httpServletRequest request
     * @return whether the authorities in the authentication match the one needed for the request
     */
    public boolean checkCourseOwnerByAssignmentId(Authentication authentication, HttpServletRequest httpServletRequest) {
        String assignmentIdString = httpServletRequest.getParameter("assignmentId");
        Long assignmentId = Long.parseLong(assignmentIdString);
        Assignment assignment;
        try {
            assignment = assignmentService.findById(assignmentId).get();
        } catch (NoSuchElementException e) {
            log.error("Can't find assignment with id {} when authorizing request", assignmentId);
            return false;
        }

        String courseId = assignment.getCourse().getId().toString();
        String courseOwnerAuthority = authorityStringBuilder.getCourseOwnerAuthority(courseId);
        // check if there is any authority matching this course:
        var authority = authentication.getAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals(courseOwnerAuthority))
                .findFirst();
        return authority.isPresent();
    }

    /**
     * Check if the authenticated user is the owner of the course given in the request parameter:
     * @param parameterName name of the parameter from the request where the courseId is
     * @param authentication authentication
     * @param httpServletRequest request
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
     * @param courseId id of course whose owner the user must be
     * @return whether the user is an owner of the course
     */
    public boolean checkCourseOwner(User user, Long courseId) {
        String courseOwnerAuthorityName = authorityStringBuilder.getCourseOwnerAuthority(courseId.toString());
        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equals(courseOwnerAuthorityName));
    }

    /**
     * Checks if a user is enrolled in a course
     * @param user user to be checked
     * @param courseId id of course where the user is verified to be enrolled
     * @return whether the user is enrolled in the course
     */
    public boolean checkCourseEnrolled(User user, Long courseId) {
        String courseOwnerAuthorityName = authorityStringBuilder.getCourseEnrolledAuthority(courseId.toString());
        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equals(courseOwnerAuthorityName));
    }
}
