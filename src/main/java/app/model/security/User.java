package app.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"password","hibernateLazyInitializer", "handler"})
public class User
{
    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;


    public User(String username, String password, Role... roles) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.roles = Arrays.asList(roles);
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public String[] getRolesAsStringArray() {
        return roles.stream().map(Role::getName).toArray(String[]::new);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}