package edplatform.edplat.entities.grading;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.grading.questions.QuizQuestion;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Course course;

    private String quizName;

    private Integer gradeWeight;

    private Boolean isComplete;

    @OneToMany(mappedBy = "quiz",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<QuizQuestion> quizQuestions;
}
