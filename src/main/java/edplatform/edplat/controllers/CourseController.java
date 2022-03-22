package edplatform.edplat.controllers;


import edplatform.edplat.courses.Course;
import edplatform.edplat.courses.CourseRepository;
import edplatform.edplat.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@Controller
@RequestMapping(path="/")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

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
