package edplatform.edplat.entities.courses.enrollment;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.users.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "courseJoinRequests")
public class CourseJoinRequest {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Course course;
}
