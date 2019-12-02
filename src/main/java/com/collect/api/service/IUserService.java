package com.collect.api.service;

import com.collect.api.model.User;

public interface IUserService {
	public User findByUsername(String username);

}
