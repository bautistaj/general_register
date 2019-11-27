package com.collect.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="clients")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String firstLastName;
	
	private String secondLastName;
	
	@Column(nullable = false)
	private String phone;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private Date birthday; 
	
	@Column(nullable = false)
	private Date createdAt;
	
	private String photo;
}
