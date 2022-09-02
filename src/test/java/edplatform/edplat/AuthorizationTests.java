package edplatform.edplat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTests {

    @Autowired
    private MockMvc mvc;

    // "/assignment/new" - needs "course-{id}-owner" authority
    @Test
    void ShouldRedirectToLoginPageNewAssignmentUnauthenticated() throws Exception {
        mvc.perform(get("/assignment/new"))
                .andExpect(status().is3xxRedirection()) // redirect (302)
                .andExpect(redirectedUrl("http://localhost/login")); // redirect URL
    }
}
