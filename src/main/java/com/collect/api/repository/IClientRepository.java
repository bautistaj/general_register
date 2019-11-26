package com.collect.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collect.api.model.Client;

public interface IClientRepository extends JpaRepository<Client, Long> {

}
