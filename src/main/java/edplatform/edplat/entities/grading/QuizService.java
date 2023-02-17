package edplatform.edplat.entities.grading;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.grading.questions.FreeAnswerQuestion;
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
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private FreeAnswerQuestionRepository freeAnswerQuestionRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

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
    public void loadQuestions(Quiz quiz) {
        quiz.setQuizQuestions(quizQuestionRepository.findAllByQuiz(quiz));
    }
}
