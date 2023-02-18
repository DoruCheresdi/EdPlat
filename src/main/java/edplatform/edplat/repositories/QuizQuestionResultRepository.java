package edplatform.edplat.repositories;

import edplatform.edplat.entities.grading.questions.QuizQuestion;
import edplatform.edplat.entities.grading.questions.QuizQuestionResult;
import edplatform.edplat.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizQuestionResultRepository extends JpaRepository<QuizQuestionResult, Long> {

    Optional<QuizQuestionResult> findByUserAndAndQuizQuestion(User user, QuizQuestion quizQuestion);
}
