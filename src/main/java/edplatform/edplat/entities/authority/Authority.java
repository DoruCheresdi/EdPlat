package edplatform.edplat.entities.authority;

import edplatform.edplat.entities.users.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "users_authorities",
            joinColumns = { @JoinColumn(name = "authorities_id") },
            inverseJoinColumns = { @JoinColumn(name = "users_id") }
    )
    private Set<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
