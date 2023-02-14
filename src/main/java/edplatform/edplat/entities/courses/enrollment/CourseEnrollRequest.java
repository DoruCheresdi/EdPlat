package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "courseEnrollRequests")
public class CourseEnrollRequest {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Course course;

    private Timestamp createdAt;
}
