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
import edplatform.edplat.utils.FilePathBuilder;
import edplatform.edplat.utils.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AssignmentController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    SubmissionService submissionService;

    @Autowired
    FilePathBuilder filePathBuilder;

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

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // create submission:
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setUser(user);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        submission.setSubmissionResource(fileName);
        // save submission file:
        String uploadDir = filePathBuilder.getSubmissionFileDirectory(assignment, user);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        // save submission to database:
        submissionService.save(submission);

        return new RedirectView("/course?id=" + assignment.getCourse().getId());
    }

    @PostMapping("/assignment/submission/delete")
    public RedirectView deleteSubmission(@RequestParam Long assignmentId,
                                         Authentication authentication) throws IOException {
        User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();

        Assignment assignment = assignmentService.findById(assignmentId).get();
        if (!assignmentService.hasUserSubmitted(assignment, user)) {
            log.error("Trying to delete submission for a user that has no submission on assignment");
            return new RedirectView("/course?id=" + assignment.getCourse().getId());
        }

        Submission submission = assignmentService.getSubmissionForUser(assignment, user);
        // delete submission:
        submissionService.delete(submission);

        return new RedirectView("/course?id=" + assignment.getCourse().getId());
    }
}
