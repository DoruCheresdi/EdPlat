package edplatform.edplat.controllers;

import edplatform.edplat.entities.grading.Quiz;
import edplatform.edplat.entities.grading.QuizService;
import edplatform.edplat.entities.grading.questions.FreeAnswerQuestion;
import edplatform.edplat.entities.grading.questions.SingleChoiceQuestion;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;

@Controller
@RequestMapping(path="/")
@Slf4j
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @GetMapping("/quiz/take_quiz")
    public String getNextQuestion(Model model,
                           @RequestParam Long quizId,
                           @RequestParam(required = false) Integer questionIndex, Authentication authentication) {
        if (Objects.isNull(questionIndex)) {
            questionIndex = 0;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername()).get();
        return quizService.getNextQuestion(model, quizId, questionIndex, user);
    }

    @PostMapping("quiz/process_free_answer")
    public String processFreeAnswer(Authentication authentication,
                                    @RequestParam Long quizId,
                                    @RequestParam Long questionId,
                                    @RequestParam Integer questionIndex,
                                    @RequestParam String answer,
                                    Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername()).get();

        boolean answerIsCorrect = quizService.submitAnswerFreeQuestion(user, questionId, answer);
        model.addAttribute("answerIsCorrect", answerIsCorrect);
        FreeAnswerQuestion freeAnswerQuestion = quizService.findFreeAnswerQuestionById(questionId);
        model.addAttribute("freeAnswerQuestion", freeAnswerQuestion);
        model.addAttribute("quizId", quizId);
        model.addAttribute("answer", answer);

        model.addAttribute("nextQuestionIndex", questionIndex + 1);
        return "quiz/show_answer_free_answer_question";
    }

    @PostMapping("quiz/process_single_choice")
    public String processSingleChoiceAnswer(Authentication authentication,
                                    @RequestParam Long quizId,
                                    @RequestParam Long questionId,
                                    @RequestParam Integer questionIndex,
                                    @RequestParam Long answerChoiceId,
                                    Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername()).get();

        boolean answerIsCorrect = quizService.submitSingleChoiceAnswer(user, questionId, answerChoiceId);
        model.addAttribute("answerIsCorrect", answerIsCorrect);
        SingleChoiceQuestion singleChoiceQuestion = quizService.findSingleChoiceQuestionByIdWithChoices(questionId);
        model.addAttribute("singleChoiceQuestion", singleChoiceQuestion);
        model.addAttribute("quizId", quizId);
        model.addAttribute("answerChoiceId", answerChoiceId);

        model.addAttribute("questionIndex", questionIndex);
        model.addAttribute("nextQuestionIndex", questionIndex + 1);
        return "quiz/show_answer_single_choice";
    }
}
