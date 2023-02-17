package edplatform.edplat.entities.grading;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.grading.questions.FreeAnswerQuestion;
import edplatform.edplat.entities.grading.questions.QuestionChoice;
import edplatform.edplat.entities.grading.questions.QuizQuestion;
import edplatform.edplat.entities.grading.questions.SingleChoiceQuestion;
import edplatform.edplat.repositories.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private FreeAnswerQuestionRepository freeAnswerQuestionRepository;

    @Autowired
    private SingleChoiceQuestionRepository singleChoiceQuestionRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    public void createQuiz(Long courseId, String quizName, Integer gradeWeight) {
        Quiz quiz = new Quiz();
        Course course = courseRepository.findById(courseId).get();

        quiz.setCourse(course);
        quiz.setGradeWeight(gradeWeight);
        quiz.setQuizName(quizName);

        quizRepository.save(quiz);
    }

    public List<Quiz> getQuizzesForCourse(Long courseId) {
        return quizRepository.findAllByCourseId(courseId);
    }

    public void markAsDone(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).get();
        quiz.setIsComplete(true);
        quizRepository.save(quiz);
    }

    @Transactional
    public void createFreeAnswerQuestion(FreeAnswerQuestion freeAnswerQuestion, Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).get();
        freeAnswerQuestion.setQuiz(quiz);
        quiz.getQuizQuestions().add(freeAnswerQuestion);

        freeAnswerQuestionRepository.save(freeAnswerQuestion);
        quizRepository.save(quiz);
    }

    @Transactional
    public Course getCourseForQuizId(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).get();
        return quiz.getCourse();
    }

    @Transactional
    public Course getCourseForQuestionId(Long questionId) {
        QuizQuestion question = quizQuestionRepository.findById(questionId).get();
        return question.getQuiz().getCourse();
    }

    @Transactional
    public void loadQuestions(Quiz quiz) {
        quiz.setQuizQuestions(quizQuestionRepository.findAllByQuiz(quiz));
    }


    @Transactional
    public void createSingleChoiceQuestion(SingleChoiceQuestion singleChoiceQuestion, Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).get();
        singleChoiceQuestion.setQuiz(quiz);
        quiz.getQuizQuestions().add(singleChoiceQuestion);

        singleChoiceQuestionRepository.save(singleChoiceQuestion);
        quizRepository.save(quiz);
    }

    @Transactional
    public void createChoiceForSingleChoice(QuestionChoice questionChoice, Long questionId) {
        SingleChoiceQuestion question = singleChoiceQuestionRepository.findById(questionId).get();
        questionChoice.setSingleChoiceQuestion(question);
        question.getQuestionChoices().add(questionChoice);
        questionChoiceRepository.save(questionChoice);
    }

    public List<FreeAnswerQuestion> getFreeAnswerQuestions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).get();
        return freeAnswerQuestionRepository.findAllByQuiz(quiz);
    }

    @Transactional
    public List<SingleChoiceQuestion> getSingleChoiceQuestionsWithChoices(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).get();
        List<SingleChoiceQuestion> singleChoiceQuestions = singleChoiceQuestionRepository.findAllByQuiz(quiz);

        // load the choices for each question:
        for (SingleChoiceQuestion singleChoiceQuestion : singleChoiceQuestions) {
            Hibernate.initialize(singleChoiceQuestion.getQuestionChoices());
//            singleChoiceQuestion.setQuestionChoices(questionChoiceRepository.findAllBySingleChoiceQuestion(singleChoiceQuestion));
        }

        return singleChoiceQuestions;
    }

    public Quiz findQuizById(Long quizId) {
        return quizRepository.findById(quizId).get();
    }
}
