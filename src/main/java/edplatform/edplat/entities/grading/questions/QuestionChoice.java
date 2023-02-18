package edplatform.edplat.entities.grading.questions;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class QuestionChoice {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String choiceText;

    private Boolean isCorrect;

    @ManyToOne
    private SingleChoiceQuestion singleChoiceQuestion;
}
