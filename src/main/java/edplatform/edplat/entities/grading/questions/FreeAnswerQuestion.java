package edplatform.edplat.entities.grading.questions;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
public class FreeAnswerQuestion extends QuizQuestion {

    private String answer;

    @Enumerated(EnumType.STRING)
    private AnswerType freeAnswerType;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FREE_ANSWER;
    }
}
