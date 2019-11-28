package com.collect.api.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, length = 20)
	private String username;
	
	@Column(length = 60)
	private String password;
	
	private Boolean enabled;
	
	@Column(unique = true)
	private String email;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name ="id_user"), 
	inverseJoinColumns = @JoinColumn(name = "id_role"),
	uniqueConstraints = {@UniqueConstraint(columnNames = {"id_user","id_role"})})
	private Set<Role> roles;
	
}
