package edplatform.edplat.entities.grading.questions;

import edplatform.edplat.entities.users.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class QuizQuestionResult {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * Result of the question in the range of 0-1:
     */
    Float result;

    @ManyToOne
    User user;

    @ManyToOne
    QuizQuestion quizQuestion;
}
