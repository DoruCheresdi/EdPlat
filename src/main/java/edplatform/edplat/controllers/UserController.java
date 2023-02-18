package edplatform.edplat.controllers;

import edplatform.edplat.entities.authority.AuthorityService;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseDisplay;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollRequest;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollRequestService;
import edplatform.edplat.entities.courses.enrollment.EnrollRequestViewDTO;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import edplatform.edplat.security.SecurityAuthorizationChecker;
import edplatform.edplat.utils.FileUploadUtil;
import edplatform.edplat.utils.TimePrettier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.*;

@Controller
@RequestMapping(path="/")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private TimePrettier timePrettier;

    @Autowired
    private SecurityAuthorizationChecker securityAuthorizationChecker;

    @Autowired
    private CourseEnrollRequestService courseEnrollRequestService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        userService.encryptPassword(user);

        userService.save(user);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsersByPage(Model model, @RequestParam(required = false) Integer pageNumber) {
        // default page size is 10:
        int pageSize = 5;

        // if pageNumber is not present in URL, set it to default:
        if (pageNumber == null) {
            pageNumber = 0;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> listUsers = userService.findAll(pageable);
        model.addAttribute("listUsers", listUsers);

        model.addAttribute("currentPageNumber", pageNumber);
        model.addAttribute("numberPages", listUsers.getTotalPages());

        return "users_paged";
    }

    @GetMapping("/user/edit")
    public String showUserEditForm(Model model) {
        return "edit_user";
    }

    @GetMapping("/user/enroll_requests")
    public String showUserEnrollRequests(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername()).get();

        // get names of the courses that the user tries to enroll in:
        List<CourseEnrollRequest> courseEnrollRequests = courseEnrollRequestService.findAllByUser(user);
        List<EnrollRequestViewDTO> courseEnrollmentRequestsDTO = new ArrayList<>();

        for (CourseEnrollRequest courseEnrollRequest : courseEnrollRequests) {
            // get time since course has been created in pretty format:
            String timeSinceString = timePrettier.prettyTimestamp(courseEnrollRequest.getCreatedAt());

            courseEnrollmentRequestsDTO.add(
                    new EnrollRequestViewDTO(courseEnrollRequest.getCourse().getCourseName(),
                            timeSinceString)
            );
        }

        model.addAttribute("courseEnrollmentRequests", courseEnrollmentRequestsDTO);
        return "enroll_requests";
    }

    @PostMapping("/user/process_img_edit")
    public RedirectView savePhotoToUser(@RequestParam("image") MultipartFile multipartFile,
                                        Authentication authentication) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        // save image name to database:
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername()).get();
        user.setPhoto(fileName);

        String uploadDir = "user-photos/" + user.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        userService.save(user);

        return new RedirectView("/users");
    }

    @GetMapping("/user/courses")
    public String showUserCourses(Model model,
                                  Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user;
        try {
            user = userService.findByEmailWithCourses(userDetails.getUsername()).orElseThrow();
        } catch (NoSuchElementException e) {
            log.info("Can't find user");
            return "error";
        }
        List<Course> userCourses = user.getCourses();
        // load authorities:
        user.setAuthorities(new HashSet<>(authorityService.findAllByUserEmail(user.getEmail())));

        List<CourseDisplay> userCoursesDisplay = new ArrayList<>();
        for (Course course :
                userCourses) {
            String userAuthority;
            if (securityAuthorizationChecker.checkCourseOwner(user, course.getId())) {
                userAuthority = "Owner";
            } else if (securityAuthorizationChecker.checkCourseEnrolled(user, course.getId())) {
                userAuthority = "Enrolled";
            } else {
                log.error("Error determining user authority for user {} for course with id: {}",
                        user.getEmail(), course.getId());
                userAuthority = "Unknown";
            }

            userCoursesDisplay.add(new CourseDisplay(
                    course.getId(), course.getCourseName(), course.getDescription(), course.getImage(),
                    course.getCreatedAt(), userAuthority));
        }

        model.addAttribute("userCoursesDisplay", userCoursesDisplay);
        return "user_courses";
    }
}
