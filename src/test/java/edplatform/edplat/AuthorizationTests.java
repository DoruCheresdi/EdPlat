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
    @DisplayName("\"/assignment/new\" - needs \"course-{id}-owner\" authority")
    void shouldAuthorizeNewAssignment() throws Exception {
        // create user with authority:
        UserDetails userDetails = getUserDetailsWithAuthority("course-3-owner");

        mvc.perform(get("/assignment/new").param("courseId", "3")
                        .with(user(userDetails)))
                .andExpect(view().name("add_assignment"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("\"/assignment/new\" - needs \"course-{id}-owner\" authority - Forbidden")
    void shouldNotAuthorizeNewAssignment() throws Exception {
        // create user with authority:
        UserDetails userDetails = getUserDetailsWithAuthority("course-4-owner");

        // should fail because it expects "course-3-owner", is given "course-4-owner" instead:
        mvc.perform(get("/assignment/new").param("courseId", "3")
                        .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("\"/course/edit\" - needs \"course-{id}-owner\" authority")
    void shouldAuthorizeCourseEdit() throws Exception {
        // create user with authority:
        UserDetails userDetails = getUserDetailsWithAuthority("course-3-owner");

        mvc.perform(get("/course/edit").param("id", "3")
                        .with(user(userDetails)))
                .andExpect(view().name("edit_course"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("\"/course/edit\" - needs \"course-{id}-owner\" authority - Forbidden")
    void shouldNotAuthorizeCourseEdit() throws Exception {
        // create user with authority:
        UserDetails userDetails = getUserDetailsWithAuthority("course-4-owner");

        // should fail because it expects "course-3-owner", is given "course-4-owner" instead:
        mvc.perform(get("/course/edit").param("id", "3")
                        .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    /**
     * method that returns a UserDetails with an application entity user and
     * gives it an authority with the name authorityName
     * @param authorityName name of authority needed
     * @return User details with authority authorityName
     */
    private UserDetails getUserDetailsWithAuthority(String authorityName) {
        User user = new User();
        Authority authority = new Authority();
        authority.setName(authorityName);
        user.setAuthorities(List.of(authority));
        user.setFirstName("testLastName");
        user.setLastName("testLastName");

        return new CustomUserDetails(user);
    }
}
