package edplatform.edplat.repositories;

import edplatform.edplat.entities.grading.Quiz;
import edplatform.edplat.entities.grading.questions.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findAllByQuiz(Quiz quiz);
}
