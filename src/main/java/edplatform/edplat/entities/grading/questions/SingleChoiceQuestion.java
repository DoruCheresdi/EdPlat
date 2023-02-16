package edplatform.edplat.entities.grading.questions;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class SingleChoiceQuestion extends QuizQuestion {

    @OneToMany
    private List<QuestionChoice> questionChoices;
}
