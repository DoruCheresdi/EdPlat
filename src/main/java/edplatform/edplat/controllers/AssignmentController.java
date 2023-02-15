package edplatform.edplat.controllers;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.assignment.AssignmentService;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import edplatform.edplat.entities.submission.Submission;
import edplatform.edplat.entities.submission.SubmissionService;
import edplatform.edplat.entities.users.CustomUserDetails;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.utils.FilePathBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@Slf4j
public class AssignmentController {

    @Autowired
    private CourseService courseService;

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
        try {
            courseService.addAssignmentToCourse(assignment, courseId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/error");
        }

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
                                         Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        try {
            assignmentService.addSubmission(multipartFile, assignmentId, user);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("error");
        }

        Assignment assignment = assignmentService.findById(assignmentId).get();
        return new RedirectView("/course?id=" + assignment.getCourse().getId());
    }

    @GetMapping(value = "/assignment/submission/download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] downloadSubmission(@RequestParam Long assignmentId,
                                            Authentication authentication) throws IOException {
        // get submission:
        User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();
        Submission submission = assignmentService.getSubmissionForUser(assignmentId, user.getId());
        // get file input stream:
        File initialFile = new File(submission.getResourcePath());
        InputStream targetStream = new FileInputStream(initialFile);
        return IOUtils.toByteArray(targetStream);
    }

    @PostMapping("/assignment/submission/delete")
    public RedirectView deleteSubmission(@RequestParam Long assignmentId,
                                         Authentication authentication) throws IOException {
        User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();

        Assignment assignment;
        try {
            assignment = assignmentService.findWithSubmissionsById(assignmentId).orElseThrow();
        } catch (NoSuchElementException e) {
            log.error("Can't find assignment with id {}", assignmentId);
            return new RedirectView("/error");
        }

        if (!assignmentService.hasUserSubmitted(assignment, user)) {
            log.error("Trying to delete submission for a user that has no submission on assignment");
            return new RedirectView("/course?id=" + assignment.getCourse().getId());
        }

        Submission submission = assignmentService.getSubmissionForUser(assignment.getId(), user.getId());
        // delete submission:
        submissionService.delete(submission);

        return new RedirectView("/course?id=" + assignment.getCourse().getId());
    }

    @GetMapping("/assignment/edit")
    public String getAssignmentEditPage(@RequestParam Long assignmentId,
                                        Model model) {
        model.addAttribute("assignmentId", assignmentId);
        return "edit_assignment";
    }

    @PostMapping("/assignment/change_description")
    public RedirectView changeAssignmentName(@RequestParam Long assignmentId,
                                             @RequestParam String newDescription) {
        Optional<Assignment> optionalAssignment = assignmentService.findById(assignmentId);
        Assignment assignment;
        if (optionalAssignment.isPresent()) {
            assignment = optionalAssignment.get();
        } else {
            return new RedirectView("error");
        }

        assignment.setDescription(newDescription);
        assignmentService.save(assignment);

        String courseId = assignment.getCourse().getId().toString();
        return new RedirectView("/course?id=" + courseId);
    }

    @GetMapping("/assignment/submissions")
    public String showSubmissionsForAssignment(Model model,
                                               @RequestParam Long assignmentId) {
        List<Submission> submissionList = submissionService.findAllByAssignmentIdWithUser(assignmentId);
        model.addAttribute("submissions", submissionList);

        return "submissions";
    }

    @PostMapping("/assignment/submission/add_grade")
    public RedirectView showSubmissionsForAssignment(Model model,
                                               @RequestParam Float grade,
                                               @RequestParam Long submissionId) {
        submissionService.updateGrade(submissionId, grade);

        Assignment assignment = submissionService.getAssignmentForSubmissionId(submissionId);
        return new RedirectView("/assignment/submissions?assignmentId=" + assignment.getId());
    }
}
