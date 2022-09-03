package edplatform.edplat;

import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTests {

    @Autowired
    private MockMvc mvc;

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
        mvc.perform(get("/assignment/new").param("courseId", "3")
                        .with(user("testUser").password("pass")
                                .authorities(new SimpleGrantedAuthority("course-3-owner"))))
                .andExpect(view().name("add_assignment"));
    }
}
