package edplatform.edplat.users;

import edplatform.edplat.courses.Course;
import edplatform.edplat.submission.Submission;
import lombok.Data;
import net.bytebuddy.build.Plugin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

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

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/user-photos/" + id + "/" + photo;
    }
}
