package edplatform.edplat.entities.users;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.submission.Submission;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String institution;

    private String photo;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL)
    private List<Submission> submissions;

    @ManyToMany(targetEntity = Course.class)
    private List<Course> courses;

    @ManyToMany(targetEntity = Authority.class, fetch = FetchType.EAGER)
    private List<Authority> authorities;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/user-photos/" + id + "/" + photo;
    }
}
