package com.collect.api.service;

import java.util.List;

import com.collect.api.model.File;

public interface IFileService {
	public File save (File file);
	public File findById (Long id);
	public List<File> findByIdClient (Long id);
	public void delete (File file);
}
