package edplatform.edplat.controllers;


import edplatform.edplat.entities.authority.AuthorityRepository;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseRepository;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import edplatform.edplat.security.AuthorityStringBuilder;
import edplatform.edplat.security.AuthorityService;
import edplatform.edplat.utils.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

@Controller
@RequestMapping(path="/")
@Slf4j
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityStringBuilder authorityStringBuilder;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/course/new")
    public String showCourseCreationForm(Model model) {
        model.addAttribute("course", new Course());

        return "create_course_form";
    }

    @PostMapping("/course/process_course")
    public String processCourse(Course course, Authentication authentication) {
        // add the time it was added to the course:
        Timestamp courseCreatedAt = new Timestamp(System.currentTimeMillis());
        course.setCreatedAt(courseCreatedAt);

        courseRepository.save(course);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());

        // add the course owner authority to the user that created the course:
        String authorityName = authorityStringBuilder.getCourseOwnerAuthority(course.getId().toString());
        authorityService.giveAuthorityToUser(user, authorityName);

        return "course_creation_success";
    }

    @GetMapping("/course/courses")
    public String listCourses(Model model) {
        Iterable<Course> listCourses = courseRepository.findAll();
        model.addAttribute("listCourses", listCourses);

        return "courses";
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

    @PostMapping("course/enroll")
    public RedirectView enrollUserInCourse(@RequestParam Long courseId,
                                     Authentication authentication) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            // TODO add error page:
            return new RedirectView("course/coursesssss");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user.getCourses().contains(course)) {
            return new RedirectView("/course/courses");
        }
        user.getCourses().add(course);

        // add the course owner authority to the user that created the course:
        String authorityName = authorityStringBuilder.getCourseEnrolledAuthority(course.getId().toString());
        authorityService.giveAuthorityToUser(user, authorityName);

        return new RedirectView("/course/courses");
    }

    @GetMapping("/course")
    public String showCourseAssignments(
            @RequestParam Long id,
            Model model) {
        log.info("Showing single course");
        Optional<Course> optionalCourse = courseRepository.findById(id);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            // TODO add error page:
            return "courses";
        }

        model.addAttribute("course", course);
        model.addAttribute("courseId", id);
        model.addAttribute("assignments", course.getAssignments());
        return "course";
    }

    @PostMapping("course/delete")
    public String deleteCourse(@RequestParam Long courseId,
                                           Authentication authentication) {
        // find the course:
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            return "error";
        }

        // delete all authorities regarding course:
        // for owners:
        String ownerAuthority = authorityStringBuilder.getCourseOwnerAuthority(courseId.toString());
        authorityService.deleteAuthority(ownerAuthority);
        // for enrolled:
        String enrolledAuthority = authorityStringBuilder.getCourseEnrolledAuthority(courseId.toString());
        authorityService.deleteAuthority(enrolledAuthority);

        // delete all assignments and the course itself (delete is cascading):
        courseRepository.delete(course);

        return "course_deletion_success";
    }
}
