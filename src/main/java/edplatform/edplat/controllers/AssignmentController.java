package edplatform.edplat.controllers;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.assignment.AssignmentRepository;
import edplatform.edplat.entities.assignment.AssignmentService;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseRepository;
import edplatform.edplat.entities.submission.Submission;
import edplatform.edplat.entities.submission.SubmissionRepository;
import edplatform.edplat.entities.submission.SubmissionService;
import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import edplatform.edplat.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Optional;

@Controller
public class AssignmentController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    SubmissionService submissionService;

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
            return new RedirectView("error");
        }

        assignment.setCourse(course);
        assignmentService.save(assignment);

        return new RedirectView("/course?id=" + assignment.getCourse().getId());
    }

    @GetMapping("/assignment/submission/new")
    public String showNewSubmissionView(@RequestParam Long assignmentId,
                                        Model model) {
        model.addAttribute("assignmentId", assignmentId);

        return "add_submission";
    }

    @PostMapping("/assignment/submission/new")
    public RedirectView createSubmission(@RequestParam("resource") MultipartFile multipartFile,
                                         @RequestParam Long assignmentId,
                                         Authentication authentication) throws IOException {
        Optional<Assignment> optionalAssignment = assignmentService.findById(assignmentId);
        Assignment assignment;
        if (optionalAssignment.isPresent()) {
            assignment = optionalAssignment.get();
        } else {
            return new RedirectView("error");
        }

        // save submission file name to database:
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        // create submission:
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setUser(user);
        submission.setSubmissionResource(fileName);

        String uploadDir = "submissions/" + assignment.getId() + "-" + user.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        submissionService.save(submission);

        return new RedirectView("/course?id=" + assignment.getCourse().getId());
    }
}
