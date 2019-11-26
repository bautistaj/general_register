package com.collect.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import com.collect.api.model.Client;

public interface IClientService {
	public List<Client> findAll();
	public Page<Client> findAll(Pageable pageable);
	public Client save (Client client);
	public Client findById (Long id);
	public void delete (Long id);
}
