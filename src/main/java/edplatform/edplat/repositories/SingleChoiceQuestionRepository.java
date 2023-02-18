package edplatform.edplat.repositories;

import edplatform.edplat.entities.grading.Quiz;
import edplatform.edplat.entities.grading.questions.FreeAnswerQuestion;
import edplatform.edplat.entities.grading.questions.SingleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SingleChoiceQuestionRepository extends JpaRepository<SingleChoiceQuestion, Long>  {

    List<SingleChoiceQuestion> findAllByQuiz(Quiz quiz);
}
