package edplatform.edplat.entities.grading.questions;

import edplatform.edplat.entities.users.User;

import javax.persistence.*;

@Entity
public class QuizQuestionResult {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    Float result;

    @ManyToOne
    User user;

    @ManyToOne
    QuizQuestion quizQuestion;
}
