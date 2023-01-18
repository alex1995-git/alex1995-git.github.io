package com.depcue.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depcue.model.Puerta;
import com.depcue.repository.IPuertaRepository;

@RestController
@CrossOrigin()
@RequestMapping("/puertas")
public class RestPuertaController {

	@Autowired
	private IPuertaRepository repo;

	@GetMapping
	public List<Puerta> listarPuerta() {
		return repo.findAll();
	}

	@PostMapping
	public void insertarPuerta(@RequestBody Puerta puerta) {
		repo.save(puerta);
	}

	@PutMapping
	public void actualizarPuerta(@RequestBody Puerta puerta) {
		repo.save(puerta);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminarPuerta(@PathVariable("id") Long id) {
		repo.deleteById(id);
	}
}
