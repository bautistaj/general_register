package com.collect.api.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.collect.api.model.User;
import com.collect.api.repository.IUserRepository;

public class UserService implements UserDetailsService{
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private IUserRepository userRepository;
	
	@Transactional(readOnly=true)
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username);

		if (user == null) {
			logger.error("Error login");
			throw new UsernameNotFoundException("User dont existt");
		}

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> logger.info("Role: " + authority.getAuthority())).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				user.getEnabled(), true, true, true, authorities);
	}
	
}
