package edplatform.edplat;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc()
public class CourseControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldGetListContainingAllCourses() throws Exception {
        Course course = new Course();
        course.setCourseName("TestControllerCourse");
        course.setDescription("TestCourseControllerDescription");

//        mvc.perform(post("/course/process_course").param("courseName", course.getCourseName())
//                        .param("description",  course.getDescription()))
//                .andExpect(status().isOk());
        UserDetails userDetails = getUserDetails();

        mvc.perform(get("/course/courses").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"));
    }

    @Test
    @Transactional
    @WithMockUser
    void shouldCreateAndPersistRightCourse() throws Exception {
        Course course = new Course();
        course.setCourseName("TestControllerCourse");
        course.setDescription("TestCourseControllerDescription");

//        mvc.perform(post("/course/process_course").param("courseName", course.getCourseName())
//                        .param("description",  course.getDescription()))
//                .andExpect(status().isOk());
        UserDetails userDetails = getUserDetails();

        mvc.perform(post("/course/courses").with(user(userDetails)))
                .andExpect(status().isOk());
    }

    private UserDetails getUserDetails() {
        User user = new User();
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");

        return new CustomUserDetails(user);
    }
}
