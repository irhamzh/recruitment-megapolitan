package megapolitan.recruitment.webrecruitment.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import megapolitan.recruitment.webrecruitment.model.AdminModel;
import megapolitan.recruitment.webrecruitment.repository.AdminDb;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private AdminDb adminDb;

     @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         AdminModel admin = adminDb.findByEmail(email);
         Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
         grantedAuthorities.add(new SimpleGrantedAuthority("Admin"));
         return new User(admin.getEmail(), admin.getPassword(), grantedAuthorities);
     }

}
