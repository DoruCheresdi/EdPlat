package edplatform.edplat.controllers;

import edplatform.edplat.assignment.Assignment;
import edplatform.edplat.assignment.AssignmentRepository;
import edplatform.edplat.courses.Course;
import edplatform.edplat.courses.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
public class AssignmentController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @GetMapping("/assignment/new")
    public String showNewAssignmentView(@RequestParam Long courseId,
                                        Model model) {
        model.addAttribute("courseId", courseId);



        model.addAttribute("assignment", new Assignment());

        return "add_assignment";
    }

    @PostMapping("/assignment/new")
    public RedirectView createAssignment(Assignment assignment,
                                         @RequestParam Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Course course;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        } else {
            // TODO add error page:
            return new RedirectView("course/courses");
        }

        assignment.setCourse(course);
        assignmentRepository.save(assignment);

        return new RedirectView("/course?id=" + assignment.getCourse().getId());
    }
}
