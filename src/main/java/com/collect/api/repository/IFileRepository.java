package com.collect.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.collect.api.model.File;

public interface IFileRepository  extends CrudRepository<File, Long>{
	@Query("select f from File f where f.client.id=?1")
	public List<File> findByIdClient(Long id);
}
