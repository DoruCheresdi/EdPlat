package edplatform.edplat.entities.grading;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.grading.questions.*;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.exceptions.NotAllQuestionsAnsweredQuizException;
import edplatform.edplat.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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

    @Autowired
    private FreeAnswerComparator freeAnswerComparator;

    @Autowired
    private SingleChoiceAnswerComparator singleChoiceAnswerComparator;

    @Autowired
    private QuizQuestionResultRepository quizQuestionResultRepository;

    @Autowired
    private AverageGradeCalculator averageGradeCalculator;

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
        }

        return singleChoiceQuestions;
    }

    public Quiz findQuizById(Long quizId) {
        return quizRepository.findById(quizId).get();
    }

    public FreeAnswerQuestion findFreeAnswerQuestionById(Long quizId) {
        return freeAnswerQuestionRepository.findById(quizId).get();
    }

    @Transactional
    public SingleChoiceQuestion findSingleChoiceQuestionByIdWithChoices(Long quizId) {
        SingleChoiceQuestion singleChoiceQuestion = singleChoiceQuestionRepository.findById(quizId).get();
        Hibernate.initialize(singleChoiceQuestion.getQuestionChoices());
        return singleChoiceQuestion;
    }

    /**
     * Returns name of the view and the model to be rendered for
     * the question during a quiz
     * @param model controller's model
     * @param quizId
     * @return name of the view
     */
    @Transactional
    public String getNextQuestion(Model model, Long quizId, Integer questionIndex, User user) {
        Quiz quiz = quizRepository.findById(quizId).get();

        if (questionIndex >= quiz.getQuizQuestions().size()) {
            // quiz is finished:
            return showQuizResult(model, quizId, user);
        }

        QuizQuestion question = quiz.getQuizQuestions().get(questionIndex);
        model.addAttribute("quizId", quizId);
        model.addAttribute("questionIndex", questionIndex);

        switch (question.getQuestionType()) {
            case FREE_ANSWER:
                return renderQuestionDuringQuiz(model, (FreeAnswerQuestion) question);
            case SINGLE_CHOICE:
                return renderQuestionDuringQuiz(model, (SingleChoiceQuestion) question);
        }

        return "error";
    }

    @Transactional
    public String showQuizResult(Model model, Long quizId, User user) {
        model.addAttribute("quizId", quizId);

        Quiz quiz = quizRepository.findById(quizId).get();
        model.addAttribute("quiz", quiz);
        Float averageQuizGrade = null;
        try {
            averageQuizGrade = averageGradeCalculator.calculateQuizAverageGrade(quiz, user);
        } catch (NotAllQuestionsAnsweredQuizException e) {
            // not all the questions in the quiz were answered:
            return "error";
        }
        model.addAttribute("averageQuizGrade", averageQuizGrade);

        model.addAttribute("quizGradeWeight", quiz.getGradeWeight());

        // get courseId for button:
        Course course = getCourseForQuizId(quizId);
        model.addAttribute("courseId", course.getId());

        return "quiz/finished_quiz";
    }

    public String renderQuestionDuringQuiz(Model model, SingleChoiceQuestion question) {
        // shuffle choices:
        Collections.shuffle(question.getQuestionChoices());
        question.setQuestionChoices(question.getQuestionChoices());

        model.addAttribute("singleChoiceQuestion", question);
        return "quiz/take_single_choice_question";
    }

    public String renderQuestionDuringQuiz(Model model, FreeAnswerQuestion question) {
        model.addAttribute("freeAnswerQuestion", question);
        return "quiz/take_free_answer_question";
    }

    public void changeQuestionResultForUser(User user, Long questionId, boolean answerIsCorrect,
                                            QuizQuestion question) {
        // store answer in database:
        QuizQuestion quizQuestion = quizQuestionRepository.findById(questionId).get();
        Optional<QuizQuestionResult> quizQuestionResultOptional =
                quizQuestionResultRepository.findByUserAndAndQuizQuestion(user, quizQuestion);
        QuizQuestionResult quizQuestionResult;

        // if question wasn't answered before, create new result:
        quizQuestionResult = quizQuestionResultOptional.orElseGet(QuizQuestionResult::new);

        if (answerIsCorrect) {
            quizQuestionResult.setResult(1f);
        } else {
            quizQuestionResult.setResult(0f);
        }
        quizQuestionResult.setQuizQuestion(question);
        quizQuestionResult.setUser(user);
        quizQuestionResultRepository.save(quizQuestionResult);
    }

    /**
     * Submits and answer, returns whether the answer was correct
     * @param user
     * @param questionId
     * @param answer
     * @return whether the answer was correct
     */
    @Transactional
    public boolean submitAnswerFreeQuestion(User user, Long questionId, String answer) {
        FreeAnswerQuestion question = freeAnswerQuestionRepository.findById(questionId).get();
        boolean answerIsCorrect = freeAnswerComparator.isCorrect(answer, question.getFreeAnswerType(), question.getAnswer());

        changeQuestionResultForUser(user, questionId, answerIsCorrect, question);

        return answerIsCorrect;
    }


    /**
     * Submits and answer, returns whether the answer was correct
     * @param user
     * @param questionId
     * @param answerChoiceId
     * @return whether the answer was correct
     */
    @Transactional
    public boolean submitSingleChoiceAnswer(User user, Long questionId, Long answerChoiceId) {
        SingleChoiceQuestion question = singleChoiceQuestionRepository.findById(questionId).get();
        boolean answerIsCorrect = singleChoiceAnswerComparator.isCorrect(answerChoiceId);

        changeQuestionResultForUser(user, questionId, answerIsCorrect, question);

        return answerIsCorrect;
    }
}
