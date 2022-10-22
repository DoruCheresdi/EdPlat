package edplatform.edplat.controllers;


import edplatform.edplat.entities.assignment.AssignmentService;
import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import edplatform.edplat.security.AuthorityStringBuilder;
import edplatform.edplat.security.SecurityAuthorizationChecker;
import edplatform.edplat.utils.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(path="/")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AuthorityStringBuilder authorityStringBuilder;

    @Autowired
    private SecurityAuthorizationChecker securityAuthorizationChecker;

    @GetMapping("/course/new")
    public String showCourseCreationForm(Model model) {
        model.addAttribute("course", new Course());

        return "create_course_form";
    }

    @PostMapping("/course/process_course")
    public String processCourse(Course course, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername()).get();

        // add course to user:
        course.setUsers(new ArrayList<>());
        courseService.createCourse(user, course);

        // update authorities on the authenticated user:
        Authority authority = new Authority(
                authorityStringBuilder.getCourseOwnerAuthority(course.getId().toString()));
        addAuthorityToAuthentication(authority, authentication);

        return "course_creation_success";
    }

    @GetMapping("/course/courses")
    public String listCoursesByPage(Model model, @RequestParam(required = false) Integer pageNumber) {
        // default page size is 5:
        int pageSize = 5;

        // if pageNumber is not present in URL, set it to default:
        if (pageNumber == null) {
            pageNumber = 0;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Course> listCourses = courseService.findAll(pageable);
        model.addAttribute("listCourses", listCourses);

        model.addAttribute("currentPageNumber", pageNumber);
        model.addAttribute("numberPages", listCourses.getTotalPages());

        return "courses_paged";
    }

    @GetMapping("/course/edit")
    public String showCourseEditForms(
            @RequestParam Long id,
            Model model) {
        model.addAttribute("CourseId", id);

        // get course to get its name:
        Optional<Course> optionalCourse = courseService.findById(id);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            return "error";
        }

        model.addAttribute("courseName", course.getCourseName());
        return "edit_course";
    }

    @PostMapping("/course/process_img_edit")
    public RedirectView saveImageToCourse(@RequestParam("image") MultipartFile multipartFile,
                                          @RequestParam Long id) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        // save image name to database:
        Optional<Course> optionalCourse = courseService.findById(id);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            return new RedirectView("error");
        }

        course.setImage(fileName);

        String uploadDir = "course-photos/" + course.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        courseService.save(course);

        return new RedirectView("/course/courses");
    }

    @PostMapping("/course/change_name")
    public RedirectView changeCourseName(@RequestParam Long id,
                                         @RequestParam String newCourseName) {
        Optional<Course> optionalCourse = courseService.findById(id);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            return new RedirectView("error");
        }

        course.setCourseName(newCourseName);
        courseService.save(course);

        return new RedirectView("/course/courses");
    }

    @PostMapping("/course/change_description")
    public RedirectView changeCourseDescription(@RequestParam Long courseId,
                                         @RequestParam String newDescription) {
        Optional<Course> optionalCourse = courseService.findById(courseId);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            return new RedirectView("error");
        }

        course.setDescription(newDescription);
        courseService.save(course);

        return new RedirectView("/course/courses");
    }

    @PostMapping("/course/enroll")
    public RedirectView enrollUserInCourse(@RequestParam Long courseId,
                                     Authentication authentication) {
        Optional<Course> optionalCourse = courseService.findById(courseId);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            return new RedirectView("error");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername()).get();
        courseService.enrollUserToCourse(course, user);

        // update authorities on the authenticated user:
        Authority authority = new Authority(
                authorityStringBuilder.getCourseEnrolledAuthority(course.getId().toString()));
        addAuthorityToAuthentication(authority, authentication);

        return new RedirectView("/course/courses");
    }

    @GetMapping("/course")
    public String showCourseAssignments(
            @RequestParam Long id,
            Authentication authentication,
            Model model) {
        log.info("Showing single course");
        Optional<Course> optionalCourse = courseService.findById(id);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            return "error";
        }

        model.addAttribute("course", course);
        model.addAttribute("courseId", id);
        model.addAttribute("assignments", course.getAssignments());
        model.addAttribute("assignmentService", assignmentService);
        User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();
        model.addAttribute("user", user);

        // return the appropriate view:
        if (securityAuthorizationChecker.checkCourseOwner(user, course.getId())) {
            return "course_owner";
        } else {
            return "course_enrolled";
        }
    }

    @PostMapping("/course/delete")
    public String deleteCourse(@RequestParam Long courseId,
                                           Authentication authentication) {
        // find the course:
        Optional<Course> optionalCourse = courseService.findById(courseId);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            log.info("Trying to delete nonexistent course, check Id");
            return "error";
        }

        courseService.deleteCourse(course);

        return "course_deletion_success";
    }

    @GetMapping("/course/search")
    public String showSearchView() {
        return "course_search";
    }

    @PostMapping("/course/search_course_name")
    public String showSearchResultsForCourseName(@RequestParam String courseName,
                                                 @RequestParam(required = false) Integer pageNumber,
                                                 Model model) {
        // default page size is 5:
        int pageSize = 5;
        // if pageNumber is not present in URL, set it to default:
        if (pageNumber == null) {
            pageNumber = 0;
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Course> listCourses = courseService.findAllByCourseNameContains(courseName, pageable);
        model.addAttribute("listCourses", listCourses);

        model.addAttribute("currentPageNumber", pageNumber);
        model.addAttribute("numberPages", listCourses.getTotalPages());

        // add search string to model for displaying on the view:
        model.addAttribute("searchName", courseName);

        return "course_search_result";
    }

    /**
     * Method to add a new authority to the current authentication
     * @param authority authority to be added
     * @param authentication authentication to be updated
     */
    private void addAuthorityToAuthentication(Authority authority,
                                              Authentication authentication) {
        // create list of updated authorities:
        List<GrantedAuthority> updatedAuthorities =
                new ArrayList<>(authentication.getAuthorities());
        updatedAuthorities.add(authority);
        // create updated authentication to replace the old one:
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);

        // add the authorities to the principal's user,
        // some methods used the authorities as stored here
        // and not the authentication:
        Set<Authority> castAuthorities = new HashSet<>();
        for (GrantedAuthority auth :
                updatedAuthorities) {
            castAuthorities.add((Authority) auth);
        }
        User userOfPrincipal = ((CustomUserDetails)newAuth.getPrincipal()).getUser();
        userOfPrincipal.setAuthorities(castAuthorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
