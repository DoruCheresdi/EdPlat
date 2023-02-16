package edplatform.edplat.entities.grading.questions;

import javax.persistence.Entity;

@Entity
public class FreeAnswerQuestion extends QuizQuestion {

    private String freeAnswerType;

    private AnswerType answer;
}
