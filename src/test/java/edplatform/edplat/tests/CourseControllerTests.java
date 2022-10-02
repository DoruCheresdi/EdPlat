package edplatform.edplat.tests;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.assignment.AssignmentRepository;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseRepository;
import edplatform.edplat.entities.submission.Submission;
import edplatform.edplat.entities.submission.SubmissionRepository;
import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import edplatform.edplat.security.AuthorityStringBuilder;
import net.bytebuddy.asm.Advice;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AuthorityStringBuilder authorityStringBuilder;

    @Test
    void shouldGetListContainingAllCourses() throws Exception {
        UserDetails userDetails = getSimpleUserDetails();

        mvc.perform(get("/course/courses/all").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"));
    }

    @Test
    @Transactional
    void shouldCreateAndPersistCourse() throws Exception {
        Course course = new Course();
        course.setCourseName("TestControllerCourse");
        course.setDescription("TestCourseControllerDescription");

        CustomUserDetails userDetails = (CustomUserDetails) getSimpleUserDetails();
        userRepository.save(userDetails.getUser());

        mvc.perform(post("/course/process_course")
                        .param("courseName", course.getCourseName())
                        .param("description",  course.getDescription())
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        Optional<Course> retrievedCourseOptional = courseRepository.findByCourseName(course.getCourseName());

        assertThat(retrievedCourseOptional.isPresent()).isEqualTo(true);

        Course retrievedCourse = retrievedCourseOptional.get();

        // check if it is the same course:
        assertThat(retrievedCourse.getDescription()).isEqualTo(course.getDescription());

        // check that the user is given the course:
        User retrievedUser = userRepository.findByEmail(userDetails.getUser().getEmail()).get();
        assertThat(retrievedUser.getCourses().get(0)).isEqualTo(retrievedCourse);

        // test that the authority given is right:
        assertThat(retrievedUser.getAuthorities().size()).isEqualTo(1);

        Long id = retrievedCourse.getId();
        String authority = authorityStringBuilder.getCourseOwnerAuthority(id.toString());
        assertThat(retrievedUser.getAuthorities().iterator().next().getAuthority()).isEqualTo(authority);
    }

    @Test
    @Transactional
    void shouldDeleteSimpleCourse() throws Exception {
        Course course = new Course();
        course.setCourseName("TestControllerCourse");
        course.setDescription("TestCourseControllerDescription");
        course.setUsers(new ArrayList<>());

        courseRepository.save(course);

        course = courseRepository.findByCourseName(course.getCourseName()).get();

        CustomUserDetails userDetails = (CustomUserDetails) getSimpleUserDetails();

        mvc.perform(post("/course/delete")
                        .param("courseId", course.getId().toString())
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("course_deletion_success"));

        // course must be deleted, therefore it must not be found in the query:
        assertThat(courseRepository.findByCourseName(course.getCourseName()).isPresent()).isEqualTo(false);
    }

    @Test
    @Transactional
    void shouldFailDeletingSimpleNonexistentCourse() throws Exception {
        Course course = new Course();
        course.setCourseName("TestControllerCourse");
        course.setDescription("TestCourseControllerDescription");
        course.setUsers(new ArrayList<>());

        courseRepository.save(course);

        course = courseRepository.findByCourseName(course.getCourseName()).get();

        CustomUserDetails userDetails = (CustomUserDetails) getSimpleUserDetails();

        // get course Id, deleting it afterwards probably guarantees
        // that there isn't a course with that id:
        Long courseId = course.getId();

        courseRepository.delete(course);

        assertThat(courseRepository.findByCourseName("TestControllerCourse").isPresent())
                .isEqualTo(false);

        // now trying to delete nonexisting course, should give error page as a response:
        mvc.perform(post("/course/delete")
                        .param("courseId", course.getId().toString())
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    @Transactional
    void shouldDeleteCourseWithAssignmentsSubmissionsAndAuthorities() throws Exception {
        // create entities:
        CustomUserDetails userDetails = (CustomUserDetails) getSimpleUserDetails();

        Course course = new Course();
        course.setCourseName("TestControllerCourse");
        course.setDescription("TestCourseControllerDescription");
        course.setUsers(Arrays.asList(userDetails.getUser()));
        userDetails.getUser().setCourses(new ArrayList<>());
        userDetails.getUser().getCourses().add(course);

        Assignment assignment = new Assignment();
        assignment.setAssignmentName("TestAssignment");
        assignment.setCourse(course);
        course.setAssignments(Arrays.asList(assignment));

        Submission submission = new Submission();
        submission.setUser(userDetails.getUser());
        submission.setSubmissionResource("submissionResource");
        submission.setAssignment(assignment);
        assignment.setSubmissions(Arrays.asList(submission));

        courseRepository.save(course);

        // retrieve entities to check them later:
        assignment = assignmentRepository.findByAssignmentName("TestAssignment").get();
        submission = assignment.getSubmissions().get(0);
        Long submissionId = submission.getId();
        assertThat(submissionRepository.findById(submissionId).isPresent()).isEqualTo(true);

        course = courseRepository.findByCourseName(course.getCourseName()).get();

        mvc.perform(post("/course/delete")
                        .param("courseId", course.getId().toString())
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("course_deletion_success"));

        // course must be deleted, therefore it must not be found in the query:
        assertThat(courseRepository.findByCourseName(course.getCourseName()).isPresent()).isEqualTo(false);
        assertThat(assignmentRepository.findByAssignmentName("TestAssignment").isPresent()).isEqualTo(false);
        assertThat(submissionRepository.findById(submissionId).isPresent()).isEqualTo(false);
    }

    private UserDetails getSimpleUserDetails() {
        User user = new User();
        user.setEmail("test@springTest.com");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setCourses(new ArrayList<>());

        return new CustomUserDetails(user);
    }
}
