package edplatform.edplat.security;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Bean used in the security filter chain to check if users have certain authorities
 */
@Component
public class SecurityAuthorizationChecker {

    @Autowired
    private AuthorityBuilder authorityBuilder;

    // check if the authenticated user is the owner of the course given in
    // the request parameter:
    public boolean checkCourseOwner(Authentication authentication, HttpServletRequest httpServletRequest) {
        String courseId = httpServletRequest.getParameter("courseId");
        String courseOwnerAuthority = authorityBuilder.getCourseOwnerAuthority(courseId);
        // check if there is any authority matching this course:
        var authority = authentication.getAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals(courseOwnerAuthority))
                .findFirst();
        return authority.isPresent();
    }
}
