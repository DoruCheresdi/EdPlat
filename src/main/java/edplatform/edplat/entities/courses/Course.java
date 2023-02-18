package edplatform.edplat.entities.courses;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollRequest;
import edplatform.edplat.entities.courses.enrollment.CourseEnrollment;
import edplatform.edplat.entities.grading.Quiz;
import edplatform.edplat.entities.users.User;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String courseName;

    private String description;

    private String image;

    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    private CourseEnrollment.EnrollmentType enrollmentType;

    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Assignment> assignments;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "users_courses",
            joinColumns = { @JoinColumn(name = "courses_id") },
            inverseJoinColumns = { @JoinColumn(name = "users_id") }
    )
    private List<User> users;

    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<CourseEnrollRequest> enrollRequests;


    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Quiz> quizzes;

    @Transient
    public String getImagePath() {
        if (image == null || id == null) return null;

        return "/course-photos/" + id + "/" + image;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Course{");
        if (id != null) {
            stringBuilder.append(", id='");
            stringBuilder.append(id);
            stringBuilder.append('\'');
        }
        if (courseName != null) {
            stringBuilder.append(", courseName='");
            stringBuilder.append(courseName);
            stringBuilder.append('\'');
        }
        if (description != null) {
            stringBuilder.append(", description='");
            stringBuilder.append(description);
            stringBuilder.append('\'');
        }
        if (image != null) {
            stringBuilder.append(", image='");
            stringBuilder.append(image);
            stringBuilder.append('\'');
        }
        if (createdAt != null) {
            stringBuilder.append(", createdAt='");
            stringBuilder.append(createdAt);
            stringBuilder.append('\'');
        }
        return stringBuilder.toString();
    }
}
