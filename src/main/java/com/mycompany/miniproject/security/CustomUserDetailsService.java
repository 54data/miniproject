package com.mycompany.miniproject.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycompany.miniproject.dao.UserDao;
import com.mycompany.miniproject.dto.UserDto;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserDao userDao;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto user = userDao.selectByUserId(username);
		if (user == null) {
			throw new UsernameNotFoundException("Bad username");
		}
		
	    if (!user.isUserStatus()) {
	        user.setUserStatus(true);
	        userDao.updateUserStatus(user);
	    }
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserRole())); 
		
		UserDetails userDetails = new CustomUserDetails(user, authorities);
		return userDetails;
	}
}
