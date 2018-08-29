package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
												//primero por la entidad y el segundo por la llave primaria Id =long	
public interface IClienteDao extends PagingAndSortingRepository <Cliente, Long> {
	
		
}
