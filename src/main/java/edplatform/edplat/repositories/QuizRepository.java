package edplatform.edplat.repositories;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.grading.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long>  {

    @Query("select q from Quiz q where q.course.id = :courseId")
    List<Quiz> findAllByCourseId(Long courseId);
}
