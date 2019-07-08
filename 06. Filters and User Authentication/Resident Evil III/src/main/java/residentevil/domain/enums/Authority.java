package residentevil.domain.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Authority implements GrantedAuthority {
    ROOT, ADMIN, MODERATOR, USER;

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public String getAuthority() {
        return ROLE_PREFIX + name();
    }

    public Set<Authority> getAuthorities() {
        return Arrays.stream(values()).skip(roleId()).collect(Collectors.toSet());
    }

    private int roleId() {
        for (int i = 0; i < values().length; i++) {
            if (values()[i] == this) return i;
        }

        return 0;
    }
}
