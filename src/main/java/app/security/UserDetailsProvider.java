package app.security;

import app.model.security.User;
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
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException
    {
        User user = users.getUserByUsername(name);
        if(user == null) throw new UsernameNotFoundException("Username not found");

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRolesAsStringArray()));
    }
}