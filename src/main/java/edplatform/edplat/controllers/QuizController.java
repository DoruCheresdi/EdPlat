package edplatform.edplat.controllers;

import edplatform.edplat.entities.courses.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path="/")
@Slf4j
public class QuizController {



    @GetMapping("/quiz/new")
    public String showQuizCreationForm(Model model,
                                       @RequestParam Long courseId) {
        model.addAttribute("courseId", courseId);
        return "quiz/new_quiz";
    }

    @PostMapping("/quiz/new")
    public String createQuiz(Model model,
                             @RequestParam Long courseId,
                             @RequestParam Float gradeWeight) {


        model.addAttribute("courseId", courseId);
        return "quiz/new_quiz";
    }

    @GetMapping("/quiz/question/new")
    public String showQuestionCreationForm(Model model,
                                           @RequestParam Long quizId) {

        model.addAttribute("quizId", quizId);
        model.addAttribute("courseId", courseId);
        return "quiz/new_quiz";
    }
}
