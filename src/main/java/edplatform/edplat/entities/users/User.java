package edplatform.edplat.entities.users;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.submission.Submission;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Submission> submissions;

    @ManyToMany(mappedBy = "users")
    private List<Course> courses;

    @ManyToMany(mappedBy = "users")
    private Set<Authority> authorities;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/user-photos/" + id + "/" + photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && email.equals(user.email) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(institution, user.institution) && Objects.equals(photo, user.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName, institution, photo);
    }
}
