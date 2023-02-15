package edplatform.edplat.controllers;


import edplatform.edplat.controllers.controllerUtils.AuthenticationUpdater;
import edplatform.edplat.entities.assignment.AssignmentService;
import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollRequest;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollRequestService;
import edplatform.edplat.entities.courses.enrollment.EnrollRequestOwnerViewDTO;
import edplatform.edplat.entities.courses.enrollment.EnrollRequestViewDTO;
import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import edplatform.edplat.exceptions.EnrollRequestAlreadySentException;
import edplatform.edplat.security.AuthorityStringBuilder;
import edplatform.edplat.security.SecurityAuthorizationChecker;
import edplatform.edplat.utils.FileUploadUtil;
import edplatform.edplat.utils.TimePrettier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private AuthenticationUpdater authenticationUpdater;

    @Autowired
    private CourseEnrollRequestService courseEnrollRequestService;

    @Autowired
    private TimePrettier timePrettier;

    @GetMapping("/course/new")
    public String showCourseCreationForm(Model model) {
        model.addAttribute("course", new Course());

        return "create_course_form";
    }

    @PostMapping("/course/process_course")
    public String processCourse(Course course, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // add course to user:
        course.setUsers(new ArrayList<>());
        courseService.createCourse(((CustomUserDetails) userDetails).getUser().getId(), course);

        // update authorities on the authenticated user:
        Authority authority = new Authority(
                authorityStringBuilder.getCourseOwnerAuthority(course.getId().toString()));
        authenticationUpdater.addAuthorityToAuthentication(authority, authentication);

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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername()).get();

        // try to enroll:
        try {
            courseService.enrollUserToCourse(courseId, user.getId(), authentication);
        } catch (EnrollRequestAlreadySentException e) {
            log.error(e.getMessage());
            return new RedirectView("/error");
        }

        return new RedirectView("/course/courses");
    }

    @PostMapping("/course/accept_enroll_request")
    public RedirectView acceptEnrollRequest(@RequestParam Long courseId,
                                            @RequestParam Long userId,
                                           Authentication authentication) {
        courseEnrollRequestService.acceptRequest(courseId, userId);

        return new RedirectView("/course?id=" + courseId);
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

        // initialize the assignments and the submissions:
        course.setAssignments(assignmentService.findAllAssignmentsWithSubmissionsByCourse(course));

        model.addAttribute("course", course);
        model.addAttribute("courseId", id);
        model.addAttribute("assignments", course.getAssignments());
        model.addAttribute("assignmentService", assignmentService);
        User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();
        model.addAttribute("user", user);

        // return the appropriate view:
        if (securityAuthorizationChecker.checkCourseOwner(user, course.getId())) {
            List<CourseEnrollRequest> courseEnrollRequestList = courseEnrollRequestService.findAllByCourse(course);
            List<EnrollRequestOwnerViewDTO> enrollRequestOwnerViewDTOS = new ArrayList<>();

            for (CourseEnrollRequest courseEnrollRequest : courseEnrollRequestList) {
                // get time since course has been created in pretty format:
                String timeSinceString = timePrettier.prettyTimestamp(courseEnrollRequest.getCreatedAt());

                enrollRequestOwnerViewDTOS.add(
                        new EnrollRequestOwnerViewDTO(courseEnrollRequest.getUser().getId().toString(),
                                courseEnrollRequest.getUser().getFirstName() + courseEnrollRequest.getUser().getLastName(),
                                timeSinceString)
                );
            }

            model.addAttribute("enrollRequestOwnerViewDTOS", enrollRequestOwnerViewDTOS);

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
            log.error("Trying to delete nonexistent course, check Id");
            return "error";
        }

        courseService.deleteCourse(course);

        return "course_deletion_success";
    }

    @GetMapping("/course/search")
    public String showSearchView() {
        return "course_search";
    }

    @GetMapping("/course/search_course_name")
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

        return "course_search_name_result";
    }

    @GetMapping("/course/search_description")
    public String showSearchResultsForDescription(@RequestParam String description,
                                                 @RequestParam(required = false) Integer pageNumber,
                                                 Model model) {
        // default page size is 5:
        int pageSize = 5;
        // if pageNumber is not present in URL, set it to default:
        if (pageNumber == null) {
            pageNumber = 0;
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Course> listCourses = courseService.findAllByDescriptionContains(description, pageable);
        model.addAttribute("listCourses", listCourses);

        model.addAttribute("currentPageNumber", pageNumber);
        model.addAttribute("numberPages", listCourses.getTotalPages());

        // add search string to model for displaying on the view:
        model.addAttribute("searchName", description);

        return "course_search_description_result";
    }
}
