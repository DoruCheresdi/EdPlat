package edplatform.edplat.entities.courses;


import edplatform.edplat.entities.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

     Optional<Course> findById(Long id);

     @QueryHints(value = { @QueryHint(name = "QueryHints.PASS_DISTINCT_THROUGH", value = "false")})
     @Query("select distinct c from Course c left join fetch c.assignments where c.id = :id")
     Optional<Course> findByIdWithAssignments(Long id);

     Optional<Course> findByCourseName(String courseName);

     Page<Course> findAllByCourseNameContains(String containsName, Pageable pageable);

     Page<Course> findAllByDescriptionContains(String containsDescription, Pageable pageable);

     @QueryHints(value = { @QueryHint(name = "QueryHints.PASS_DISTINCT_THROUGH", value = "false")})
     @Query("select distinct c from Course c join fetch c.users u where u = :user")
     List<Course> findAllByUser(User user);
}
