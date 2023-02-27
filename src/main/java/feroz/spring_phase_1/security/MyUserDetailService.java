package feroz.spring_phase_1.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import feroz.spring_phase_1.dbmodel.Privilege;
import feroz.spring_phase_1.dbmodel.Role;

@Service

public class MyUserDetailService   implements UserDetailsService{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		logger.debug("MyUserDetailService ::::: {} ",username);

        List<Role> roles = new ArrayList<>();

		return new org.springframework.security.core.userdetails.User(username,"test123", true, true, true, true, getAuthorities(roles));
	}

	


    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        collection.add(new Privilege("ROLE_MANAGER"));
        collection.add(new Privilege("ROLE_USER"));

        for (final Role role : roles) {
            privileges.add(role.getRoleType().name());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }
        
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
    		logger.debug("privilege ::::: {} ",privilege);

        	
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

	
	
}
