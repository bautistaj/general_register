package com.collect.api.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collect.api.model.File;
import com.collect.api.repository.IFileRepository;
import com.collect.api.service.IFileService;

@Service
public class FileServiceImpl implements IFileService{

	@Autowired
	private IFileRepository fileRepository;

	@Override
	public File save(File file) {
		return fileRepository.save(file);
	}

	@Override
	public File findById(Long id) {
		return fileRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(File file) {
		fileRepository.delete(file);
	}

	@Override
	public List<File> findByIdClient(Long id) {
		return fileRepository.findByIdClient(id);
	}

}
