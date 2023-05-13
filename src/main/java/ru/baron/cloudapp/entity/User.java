package ru.baron.cloudapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity(name = "usr")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "login cannot be empty")
    @Size(min = 2, max = 30, message = "login shouldn't be shorted than 2 and longer than 30 characters")
    @Pattern(regexp = "^\\S+$", message = "login cannot contain space characters")
    private String login;

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 4, max = 100, message= "password shouldn't be shorted than 4 and longer than 100 characters")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileData> files;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
