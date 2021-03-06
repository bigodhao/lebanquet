package com.aep.LeBanquet.resource;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.aep.LeBanquet.domain.BaseEntity;
import com.aep.LeBanquet.service.CrudService;

public abstract class CrudResource<T extends BaseEntity> {
	
	@Autowired
	private CrudService<T> service;
	
	public CrudService<T> getService(){
		return service;
	}
	
	@GetMapping
	public ResponseEntity<Page<T>> findAll(Pageable pageable){
		return ResponseEntity.ok(service.findAll(pageable));
	}
	
	@PostMapping
	public ResponseEntity<T> save(@RequestBody T entity) throws URISyntaxException {
		T newEntity = service.save(entity);
		return ResponseEntity.created(new URI("/" + newEntity.getId())).body(newEntity);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<T> update(@PathVariable(name="id") Long id, @RequestBody T entity) {
		
		if (service.getRepository().existsById(id)) {
			entity.setId(id);
			return ResponseEntity.ok(service.save(entity));
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado");
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable(name="id") Long id) {
		if (!service.getRepository().existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado");
		}
		
		service.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
}
