package app.security;

import app.model.User;
import app.repositories.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsProvider implements UserDetailsService
{
    private UserRepository users;

    public UserDetailsProvider(UserRepository users)
    {
        this.users = users;
    }

    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException{
        if(!users.existsById(name)) throw new UsernameNotFoundException("Username not found");
        User user = users.getOne(name);
        //Set<GrantedAuthority> roles = new HashSet<>();
        //roles.add(new SimpleGrantedAuthority("USER"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRolesAsStringArray()));
    }
}