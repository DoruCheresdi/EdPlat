package edplatform.edplat.entities.authority;

import edplatform.edplat.entities.users.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    public Authority(String name) {
        this.name = name;
    }

    public Authority() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
