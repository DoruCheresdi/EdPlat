package edplatform.edplat.entities.courses;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

     Optional<Course> findById(Long id);

     Optional<Course> findByCourseName(String courseName);

     Page<Course> findAllByCourseNameContains(String contains, Pageable pageable);
}
