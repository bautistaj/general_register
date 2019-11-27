package com.collect.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.collect.api.model.User;

public interface IUserRepository extends CrudRepository<User, Long> {
	public User findByUserName(String userName);
}
