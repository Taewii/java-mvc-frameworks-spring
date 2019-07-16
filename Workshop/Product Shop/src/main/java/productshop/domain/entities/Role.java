package productshop.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Integer id;

    @Column(nullable = false)
    private String role;

    @ManyToMany(mappedBy = "roles") // TODO: 16.7.2019 г. eager?
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return this.role;
    }
}
