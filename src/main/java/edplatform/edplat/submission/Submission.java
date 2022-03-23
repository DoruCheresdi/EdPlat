package edplatform.edplat.submission;

import edplatform.edplat.assignment.Assignment;
import edplatform.edplat.courses.Course;
import edplatform.edplat.users.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Assignment_id", nullable = false)
    Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    private String submissionResource;

    @Transient
    public String getResourcePath() {
        if (submissionResource == null || id == null) return null;

        return "/submissions/" + assignment.getId() + "-" + user.getId() + "/" + submissionResource;
    }
}
