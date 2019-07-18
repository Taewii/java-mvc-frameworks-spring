package productshop.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import productshop.domain.enums.Authority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseLongEntity implements GrantedAuthority {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public Role(Authority authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority.asRole();
    }

    public Authority authorityAsEnum() {
        return this.authority;
    }
}
