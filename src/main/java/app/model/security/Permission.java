package app.model.security;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;
}
