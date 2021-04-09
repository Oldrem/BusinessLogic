package app.model;

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
    @Id
    private String username;
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;


    public User(String username, String password, Role... roles) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.roles = Arrays.asList(roles);
    }

    public User() {}


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