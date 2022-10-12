package edplatform.edplat.entities.submission;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.users.User;
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

        return "submissions/" + assignment.getId() + "-" + user.getId() + "/" + submissionResource;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Assignment{");
        if (id != null) {
            stringBuilder.append(", id='");
            stringBuilder.append(id);
            stringBuilder.append('\'');
        }
        if (submissionResource != null) {
            stringBuilder.append(", assignmentName='");
            stringBuilder.append(submissionResource);
            stringBuilder.append('\'');
        }
        return stringBuilder.toString();
    }
}
