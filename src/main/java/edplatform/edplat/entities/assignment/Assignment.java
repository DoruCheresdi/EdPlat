package edplatform.edplat.entities.assignment;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.submission.Submission;
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
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Submission> submissions;

    private String description;

    private Integer gradeWeight;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Assignment{");
        if (id != null) {
            stringBuilder.append(", id='");
            stringBuilder.append(id);
            stringBuilder.append('\'');
        }
        if (assignmentName != null) {
            stringBuilder.append(", assignmentName='");
            stringBuilder.append(assignmentName);
            stringBuilder.append('\'');
        }
        if (course != null) {
            stringBuilder.append(", course='");
            stringBuilder.append(course);
            stringBuilder.append('\'');
        }
        if (submissions != null) {
            stringBuilder.append(", submissions='");
            stringBuilder.append(submissions);
            stringBuilder.append('\'');
        }
        if (description != null) {
            stringBuilder.append(", description='");
            stringBuilder.append(description);
            stringBuilder.append('\'');
        }
        return stringBuilder.toString();
    }
}
