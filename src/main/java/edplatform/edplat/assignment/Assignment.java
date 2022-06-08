package edplatform.edplat.assignment;

import edplatform.edplat.courses.Course;
import edplatform.edplat.submission.Submission;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String assignmentName;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @OneToMany(mappedBy = "assignment",
            cascade = CascadeType.ALL)
    private List<Submission> submissions;

    private String description;

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", courseId=" + course.getId() +
                '}';
    }
}
