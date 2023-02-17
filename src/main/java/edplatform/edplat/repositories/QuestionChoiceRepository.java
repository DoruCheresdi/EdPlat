package edplatform.edplat.repositories;

import edplatform.edplat.entities.grading.Quiz;
import edplatform.edplat.entities.grading.questions.QuestionChoice;
import edplatform.edplat.entities.grading.questions.SingleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice, Long> {

    List<QuestionChoice> findAllBySingleChoiceQuestion(SingleChoiceQuestion singleChoiceQuestion);
}
