package edplatform.edplat.repositories;

import edplatform.edplat.entities.grading.Quiz;
import edplatform.edplat.entities.grading.questions.FreeAnswerQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeAnswerQuestionRepository extends JpaRepository<FreeAnswerQuestion, Long> {

    List<FreeAnswerQuestion> findAllByQuiz(Quiz quiz);
}
