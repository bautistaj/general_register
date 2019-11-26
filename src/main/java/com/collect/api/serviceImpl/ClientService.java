package com.collect.api.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collect.api.model.Client;
import com.collect.api.repository.IClientRepository;
import com.collect.api.service.IClientService;

@Service
public class ClientService implements IClientService {

	@Autowired
	private IClientRepository clientRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return (List<Client>) clientRepository.findAll();
	}

	@Override
	public Client save(Client client) {
		return clientRepository.save(client);
	}

	@Override
	public Client findById(Long id) {
		return clientRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		clientRepository.deleteById(id);
	}

	@Override
	public Page<Client> findAll(Pageable pageable) {
		return clientRepository.findAll(pageable);
	}

}
