package edplatform.edplat.entities.courses;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

     Optional<Course> findById(Long id);

     Optional<Course> findByCourseName(String courseName);
}
