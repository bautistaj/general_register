package com.collect.api.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.collect.api.model.User;
import com.collect.api.repository.IUserRepository;
import com.collect.api.service.IUserService;

@Service
public class UserService implements IUserService, UserDetailsService{
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private IUserRepository userRepository;
	
	@Transactional(readOnly=true)
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
		
	}

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			logger.error("Error login");
			throw new UsernameNotFoundException("User dont existt");
		}

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> logger.info("Role: " + authority.getAuthority())).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getEnabled(), true, true, true, authorities);
	}
	
}
