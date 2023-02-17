package edplatform.edplat.controllers;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.grading.Quiz;
import edplatform.edplat.entities.grading.QuizService;
import edplatform.edplat.entities.grading.questions.FreeAnswerQuestion;
import edplatform.edplat.entities.grading.questions.QuestionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path="/")
@Slf4j
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/quiz/new")
    public String showQuizCreationForm(Model model,
                                       @RequestParam Long courseId) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("quiz", new Quiz());
        return "quiz/new_quiz";
    }

    @PostMapping("/quiz/new")
    public RedirectView createQuiz(@RequestParam Long courseId,
                                   Quiz quiz) {
        quizService.createQuiz(courseId, quiz.getQuizName(), quiz.getGradeWeight());

        return new RedirectView("/course?id=" + courseId);
    }

    @PostMapping("/quiz/finish")
    public RedirectView markAsComplete(@RequestParam Long courseId,
                                       Long quizId) {
        quizService.markAsDone(quizId);
        log.info("Marked quizz with id {} as complete", quizId);

        return new RedirectView("/course?id=" + courseId);
    }

    @GetMapping("/quiz/question/new/choose_type")
    public String showQuestionTypeChoosingForm(Model model,
                                               @RequestParam Long quizId) {
        model.addAttribute("quizId", quizId);
        return "quiz/choose_type";
    }

    @PostMapping("/quiz/question/new/choose_type")
    public RedirectView chooseQuestionType(Model model,
                                           @RequestParam Long quizId,
                                           @RequestParam QuestionType questionType) {
        switch (questionType) {
            case FREE_ANSWER:
                return new RedirectView("/quiz/question/new/free_answer?quizId=" + quizId);
            case SINGLE_CHOICE:
                break;
        }

        return new RedirectView("/error");
    }

    @GetMapping("/quiz/question/new/free_answer")
    public String showQuestionFormFreeAnswer(Model model,
                                             @RequestParam Long quizId) {
        FreeAnswerQuestion freeAnswerQuestion = new FreeAnswerQuestion();
        model.addAttribute("freeAnswerQuestion", freeAnswerQuestion);
        model.addAttribute("quizId", quizId);
        return "quiz/create_question_free_answer";
    }

    @PostMapping("/quiz/question/new/free_answer")
    public RedirectView processFreeAnswerQuestion(Model model,
                                            @RequestParam Long quizId,
                                            FreeAnswerQuestion freeAnswerQuestion) {
        quizService.createFreeAnswerQuestion(freeAnswerQuestion, quizId);
        Course course = quizService.getCourseForQuizId(quizId);

        return new RedirectView("/course?id=" + course.getId());
    }
}
