package edplatform.edplat;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc()
public class AuthenticationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("\"/assignment/new\" - authentication test")
    void shouldRedirectToLoginPageNewAssignmentUnauthenticated() throws Exception {
        mvc.perform(get("/assignment/new").param("courseId", "3"))
                .andExpect(status().is3xxRedirection()) // redirect (302)
                .andExpect(redirectedUrl("http://localhost/login")); // redirect URL
    }
}
