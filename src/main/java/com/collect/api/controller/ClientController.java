package com.collect.api.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.collect.api.model.Client;
import com.collect.api.service.IClientService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api")
public class ClientController {

	@Autowired
	private IClientService clientService;

	@GetMapping("/clients")
	public List<Client> index() {
		return clientService.findAll();
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/clients/page/{page}")
	public Page<Client> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 5);
		return clientService.findAll(pageable);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/clients/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Client client = null;
		Map<String, Object> response = new HashMap<>();

		try {

			client = clientService.findById(id);

		} catch (DataAccessException e) {
			response.put("message", "No se encontro registro de cliente".concat(id.toString()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (client == null) {
			response.put("message", "No se encontro registro de cliente");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping("/clients")
	public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		Client newClient = null;

		if (result.hasErrors()) {

			List<String> lstErrors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", lstErrors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			newClient = clientService.save(client);

		} catch (DataAccessException e) {
			response.put("message", "Error al crear cliente");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("message", "Cliente creado correctamente");
		response.put("client", newClient);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PutMapping("/clients/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Client client, @PathVariable Long id, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		Client currentClient = null;
		Client clientUpdated = null;

		if (result.hasErrors()) {

			List<String> lstErrors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", lstErrors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			currentClient = clientService.findById(id);

			if (currentClient == null) {
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
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("message", "Client was update");
		response.put("client", clientUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@DeleteMapping("/clients/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Client client = null;

		try {

			client = clientService.findById(id);

			if (client == null) {
				response.put("message", "No se encuantra registro de ciente");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}

			String previusPhoto = client.getPhone();

			if (previusPhoto != null && !previusPhoto.isEmpty()) {
				Path previusFilePath = Paths.get("upload").resolve(previusPhoto).toAbsolutePath();
				File previusFile = previusFilePath.toFile();

				if (previusFile.exists()) {
					previusFile.delete();
				}
			}

			clientService.delete(id);

		} catch (DataAccessException e) {
			response.put("message", "Error to delete client");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("message", "Client was delete");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping("/clients/photo")
	public ResponseEntity<?> upload(@RequestParam("photo") MultipartFile file, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		Client client = clientService.findById(id);

		if (client == null) {
			response.put("message", "Client not found");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (!file.isEmpty()) {
			String name = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
			Path filePath = Paths.get("upload").resolve(name).toAbsolutePath();

			try {

				Files.copy(file.getInputStream(), filePath);

			} catch (IOException e) {
				response.put("message", "Error to delete client");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			String previusPhoto = client.getPhoto();

			if (previusPhoto != null && !previusPhoto.isEmpty()) {
				Path previusFilePath = Paths.get("upload").resolve(previusPhoto).toAbsolutePath();
				File previusFile = previusFilePath.toFile();

				if (previusFile.exists()) {
					previusFile.delete();
				}
			}

			client.setPhoto(name);
			clientService.save(client);

		}

		response.put("message", "Client updated");
		response.put("client", client);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	@GetMapping("/clients/photo/{fileName:.+}")
	public ResponseEntity<Resource> showPhoto(@PathVariable String fileName) {

		Path filePath = Paths.get("upload").resolve(fileName).toAbsolutePath();
		Resource resource = null;

		try {

			resource = new UrlResource(filePath.toUri());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		if (!resource.exists() && !resource.isReadable()) {
			throw new RuntimeException("Can't read photo " + fileName);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
}
