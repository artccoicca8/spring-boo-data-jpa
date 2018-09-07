package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;
												//primero por la entidad y el segundo por la llave primaria Id =long	
public interface IProductoDao extends PagingAndSortingRepository <Producto, Long> {
	
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByName(String term); 
		
}
