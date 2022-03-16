package edplatform.edplat.courses;

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

    @ManyToMany(targetEntity = User.class)
    private List<User> users;
}
