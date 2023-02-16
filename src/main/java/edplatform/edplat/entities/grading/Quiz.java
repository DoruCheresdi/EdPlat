package edplatform.edplat.entities.grading;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.grading.questions.QuizQuestion;

import javax.persistence.*;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Course course;

    private Integer gradeWeight;

    @OneToMany(mappedBy = "quiz",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<QuizQuestion> quizQuestions;
}
