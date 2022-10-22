package edplatform.edplat.controllers.controllerUtils;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthenticationUpdater {

    /**
     * Method to add a new authority to the current authentication.
     * Also updated the principals' user authority list, as
     * some methods used the authorities as stored there
     * and not the authentication's
     * @param authority authority to be added
     * @param authentication authentication to be updated
     */
    public void addAuthorityToAuthentication(Authority authority,
                                              Authentication authentication) {
        // create list of updated authorities:
        List<GrantedAuthority> updatedAuthorities =
                new ArrayList<>(authentication.getAuthorities());
        updatedAuthorities.add(authority);
        // create updated authentication to replace the old one:
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
        updatePrincipalsAuthorities(updatedAuthorities, newAuth);
        // update context with new authentication:
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    private void updatePrincipalsAuthorities(List<? extends GrantedAuthority> updatedAuthorities,
                                        Authentication newAuth) {
        // add the authorities to the principal's user:
        Set<Authority> castAuthorities = new HashSet<>();
        for (GrantedAuthority auth :
                updatedAuthorities) {
            castAuthorities.add((Authority) auth);
        }
        User userOfPrincipal = ((CustomUserDetails)newAuth.getPrincipal()).getUser();
        userOfPrincipal.setAuthorities(castAuthorities);
    }

}
