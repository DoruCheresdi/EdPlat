package edplatform.edplat;

import edplatform.edplat.courses.Course;
import edplatform.edplat.courses.CourseRepository;
import edplatform.edplat.users.User;
import edplatform.edplat.users.UserRepository;
import edplatform.edplat.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path="/")
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        Iterable<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/course/new")
    public String showCourseCreationForm(Model model) {
        model.addAttribute("course", new Course());

        return "create_course_form";
    }

    @PostMapping("/course/process_course")
    public String processCourse(Course course) {

        courseRepository.save(course);

        return "course_creation_success";
    }

    @GetMapping("/course/courses")
    public String listCourses(Model model) {

        Iterable<Course> listCourses = courseRepository.findAll();
        model.addAttribute("listCourses", listCourses);

        return "courses";
    }

    @GetMapping("/user/edit")
    public String showUserEditForm(Model model) {

        return "edit_user";
    }

    @PostMapping("/user/process_img_edit")
    public RedirectView savePhotoToUser( @RequestParam("image") MultipartFile multipartFile,
                                 Authentication authentication) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        // save image name to database:
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        user.setPhoto(fileName);

        String uploadDir = "user-photos/" + user.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        userRepository.save(user);

        return new RedirectView("/users");
    }

    @GetMapping("/course/edit")
    public String showCourseEditForms(
            @RequestParam Long id,
            Model model) {
        model.addAttribute("CourseId", id);
        return "edit_course";
    }

    @PostMapping("/course/process_img_edit")
    public RedirectView saveImageToCourse(@RequestParam("image") MultipartFile multipartFile,
                                         @RequestParam Long id) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        // save image name to database:
        Optional<Course> optionalCourse = courseRepository.findById(id);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
            course.setImage(fileName);
        } else {
            // TODO add error page:
            return new RedirectView("/course/courses");
        }

        String uploadDir = "course-photos/" + course.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        courseRepository.save(course);

        return new RedirectView("/course/courses");
    }
}
