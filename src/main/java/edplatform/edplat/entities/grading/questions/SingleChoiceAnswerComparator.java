package edplatform.edplat.entities.grading.questions;

import edplatform.edplat.repositories.QuestionChoiceRepository;
import edplatform.edplat.repositories.SingleChoiceQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SingleChoiceAnswerComparator {

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    public boolean isCorrect(Long choiceId) {
        QuestionChoice questionChoice = questionChoiceRepository.findById(choiceId).get();
        return questionChoice.getIsCorrect();
    }
}
