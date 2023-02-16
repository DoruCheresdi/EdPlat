package edplatform.edplat.entities.grading.questions;

import edplatform.edplat.entities.grading.Quiz;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class QuizQuestion {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Quiz quiz;

    @OneToMany(mappedBy = "quizQuestion",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<QuizQuestionResult> quizQuestionResults;
}
