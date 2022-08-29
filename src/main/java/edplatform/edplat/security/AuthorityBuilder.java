package edplatform.edplat.security;

import org.springframework.stereotype.Component;

/**
 * Bean used to build authority strings
 */
@Component
public class AuthorityBuilder {

    public String getCourseOwnerAuthority(String Id) {
        return "course-" + Id + "-owner";
    }
}
