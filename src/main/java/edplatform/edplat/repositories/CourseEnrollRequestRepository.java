package edplatform.edplat.repositories;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollRequest;
import edplatform.edplat.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseEnrollRequestRepository extends JpaRepository<CourseEnrollRequest, Long> {

    @Query("select r from CourseEnrollRequest r where r.user.id = :userId")
    List<CourseEnrollRequest> findAllByUserId(Long userId);

    List<CourseEnrollRequest> findAllByUser(User user);
}
