package edplatform.edplat;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc()
public class AuthorizationTests {

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void beforeEach() {

    }

    @AfterEach
    void afterEach() {

    }

    @Test
    @DisplayName("\"/assignment/new\" - authentication test")
    void shouldRedirectToLoginPageNewAssignmentUnauthenticated() throws Exception {
        mvc.perform(get("/assignment/new").param("courseId", "3"))
                .andExpect(status().is3xxRedirection()) // redirect (302)
                .andExpect(redirectedUrl("http://localhost/login")); // redirect URL
    }

    @Test
    @DisplayName("\"/assignment/new\" - needs \"course-{id}-owner\" authority")
    void shouldAuthorizeNewAssignment() throws Exception {
        User user = new User();
        Authority authority = new Authority();
        authority.setName("course-3-owner");
        user.setAuthorities(List.of(authority));
        user.setFirstName("testLastName");
        user.setLastName("testLastName");

        UserDetails userDetails = new CustomUserDetails(user);

        mvc.perform(get("/assignment/new").param("courseId", "3")
                        .with(user(userDetails)))
                .andExpect(view().name("add_assignment"));
    }


    @Test
    @DisplayName("\"/assignment/new\" - needs \"course-{id}-owner\" authority - Forbidden")
    void shouldNotAuthorizeNewAssignment() throws Exception {
        User user = new User();
        Authority authority = new Authority();
        authority.setName("course-4-owner");
        user.setAuthorities(List.of(authority));
        user.setFirstName("testLastName");
        user.setLastName("testLastName");

        UserDetails userDetails = new CustomUserDetails(user);

        mvc.perform(get("/assignment/new").param("courseId", "3")
                        .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }
}
