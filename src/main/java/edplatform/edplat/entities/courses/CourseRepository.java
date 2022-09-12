package edplatform.edplat.entities.courses;

import org.springframework.data.repository.CrudRepository;

import java.lang.annotation.Native;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {

     Optional<Course> findById(Long id);

     Optional<Course> findByCourseName(String courseName);
}
