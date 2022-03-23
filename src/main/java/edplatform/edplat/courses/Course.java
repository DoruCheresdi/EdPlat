package edplatform.edplat.courses;

import edplatform.edplat.assignment.Assignment;
import edplatform.edplat.submission.Submission;
import edplatform.edplat.users.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    private String description;

    private String image;

    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL)
    private List<Assignment> assignments;

    @ManyToMany(mappedBy = "courses")
    private List<User> users;

    @Transient
    public String getImagePath() {
        if (image == null || id == null) return null;

        return "/course-photos/" + id + "/" + image;
    }
}
