package edplatform.edplat.entities.grading.questions;

import org.springframework.stereotype.Service;

@Service
public class FreeAnswerComparator {

    public boolean isCorrect(String answer, AnswerType answerType, String correctAnswer) {
        if (answerType.equals(AnswerType.STRING)) {
            return answer.toLowerCase().equals(correctAnswer.toLowerCase());
        }
        if (answerType.equals(AnswerType.FLOAT)) {
            return Math.abs(Float.parseFloat(answer) - Float.parseFloat(correctAnswer)) < 0.01;
        }
        return false;
    }
}
