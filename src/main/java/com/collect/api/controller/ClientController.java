package com.collect.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.collect.api.model.Client;
import com.collect.api.service.IClientService;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class ClientController {
	
	@Autowired
	private IClientService clientService;
	
	@GetMapping("/clients")
	public List<Client> index(){
		return clientService.findAll();
	}
	
	@GetMapping("/clients/page/{page}")
	public Page<Client> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 5);
		return clientService.findAll(pageable);
	}
	
	@GetMapping("/clients/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Client client = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			client = clientService.findById(id);
			
		} catch (DataAccessException e) {
			response.put("message", "Client id not found ".concat(id.toString()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if(client == null) {
			response.put("message", "Client id not found ".concat(id.toString()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}
	
	@PostMapping("/clients")
	public ResponseEntity<?> create(@RequestBody Client client, BindingResult result){
		Map<String, Object> response = new HashMap<>();
		Client newClient = null;
		
		if(result.hasErrors()) {
			
			List<String> lstErrors =  result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			
 			response.put("errors", lstErrors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			newClient = clientService.save(client);
			
		} catch (DataAccessException e) {
			response.put("message", "Error to create client");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		
		response.put("message", "Client created");
		response.put("client", newClient);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/clients/{id}")
	public ResponseEntity<?> update(@RequestBody Client client, @PathVariable Long id){
		Map<String, Object> response = new HashMap<>();
		Client currentClient = null;
		Client clientUpdated = null;
		
		try {
			currentClient = clientService.findById(id);
			
			if(currentClient == null) {
				response.put("message", "Client not Found");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			
			currentClient.setBirthday(client.getBirthday());
			currentClient.setCreatedAt(client.getCreatedAt());
			currentClient.setEmail(client.getEmail());
			currentClient.setFirstLastName(client.getFirstLastName());
			currentClient.setName(client.getName());
			currentClient.setPhone(client.getPhone());
			currentClient.setSecondLastName(client.getSecondLastName());
			clientUpdated = clientService.save(currentClient);
			
		} catch (DataAccessException e) {
			response.put("message", "Error to update client");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		}
		
		
		response.put("message", "Client was update");
		response.put("client", clientUpdated);
		
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
	}
	
	
	@DeleteMapping("/clients/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String, Object> response = new HashMap<>();
		Client client = null;
		
		try {
			
			client = clientService.findById(id);
			
			if(client == null) {
				response.put("message", "Client not Found");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			
			clientService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("message", "Error to delete client");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "Client was delete");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
		
	}
}
