package main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Role {
    USER(Set.of(Permission.USER)),
    MODERATOR(Set.of(Permission.USER, Permission.MODERATE));

    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuth() {
        return permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());
    }
}
