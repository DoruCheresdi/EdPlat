package edplatform.edplat.entities.grading;

import edplatform.edplat.entities.grading.questions.QuizQuestion;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.exceptions.NotAllQuestionsAnsweredQuizException;
import edplatform.edplat.repositories.QuizQuestionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AverageGradeCalculator {

    @Autowired
    private QuizQuestionResultRepository quizQuestionResultRepository;

    public float calculateCourseAverageGrade(Long userId, Long courseId) {
        return 0;
    }

    public float calculateQuizAverageGrade(Quiz quiz, User user) throws NotAllQuestionsAnsweredQuizException {
        Float avgGrade = 0f;
        for (QuizQuestion question : quiz.getQuizQuestions()) {
            var quizQuestionOptional = quizQuestionResultRepository.findByUserAndAndQuizQuestion(user, question);
            if (quizQuestionOptional.isEmpty()) {
                throw new NotAllQuestionsAnsweredQuizException("Not all questions were answered for this quiz");
            }
            avgGrade += quizQuestionResultRepository.findByUserAndAndQuizQuestion(user, question).get().getResult();
        }
        // multiply by 10 because question grades are 0 to 1:
        avgGrade = avgGrade * 10 / quiz.getQuizQuestions().size();
        return avgGrade;
    }
}
