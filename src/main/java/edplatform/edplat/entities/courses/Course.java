package edplatform.edplat.entities.courses;

import edplatform.edplat.entities.assignment.Assignment;
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
