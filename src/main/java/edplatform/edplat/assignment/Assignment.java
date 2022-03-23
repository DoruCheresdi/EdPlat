package edplatform.edplat.assignment;

import edplatform.edplat.courses.Course;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    private String description;

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", courseId=" + course.getId() +
                '}';
    }
}
