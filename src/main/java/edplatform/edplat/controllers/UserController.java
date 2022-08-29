package edplatform.edplat.controllers;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseDisplay;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import edplatform.edplat.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path="/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {

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

    @GetMapping("/user/edit")
    public String showUserEditForm(Model model) {

        return "edit_user";
    }

    @PostMapping("/user/process_img_edit")
    public RedirectView savePhotoToUser(@RequestParam("image") MultipartFile multipartFile,
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

    @GetMapping("/user/courses")
    public String showUserCourses(Model model,
                                  Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        List<Course> userCourses = user.getCourses();

        List<CourseDisplay> userCoursesDisplay = new ArrayList<>();
        for (Course course :
                userCourses) {
            userCoursesDisplay.add(new CourseDisplay(
                    course.getId(), course.getCourseName(), course.getDescription(), course.getImage(),
                    course.getCreatedAt()));
        }

        model.addAttribute("userCoursesDisplay", userCoursesDisplay);
        return "user_courses";
    }
}
