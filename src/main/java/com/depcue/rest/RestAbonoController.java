package com.depcue.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.depcue.model.Suscripcion;
import com.depcue.service.AbonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.depcue.model.Abono;

import com.depcue.repository.IAbonoRepository;
import com.depcue.repository.IRegistroAbonoRepository;

@RestController
@CrossOrigin()
@RequestMapping("/abonos")
public class RestAbonoController {

	@Autowired
	AbonoService abonoService;

	private final IAbonoRepository repAbono;
	private final IRegistroAbonoRepository repRegistroAbono;


	@Autowired
	public RestAbonoController(IAbonoRepository repAbono, IRegistroAbonoRepository repRegistroabono) {
		this.repAbono=repAbono;
		this.repRegistroAbono=repRegistroabono;
	}
	
	
	@GetMapping
	public ResponseEntity<List<Abono>> listarAbonos() {
		List<Abono> abonos = repAbono.findAll();
		return new ResponseEntity<>(abonos, HttpStatus.OK);
	}
	
	@GetMapping(value = "/activos")
	public ResponseEntity<List<Abono>> listarAbonosActivos() {
		List<Abono> abonos = repAbono.findByEstado("A");
		return new ResponseEntity<>(abonos, HttpStatus.OK);
	}

	@RequestMapping(value = "bysuscripcion", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<List<Abono>>  findAllByCsuscripcion(
			@RequestParam(value = "csuscripcion", required = true) Long csuscripcion) {
		List<Abono> items = this.abonoService.getAbonoBySuscripcion(csuscripcion);
		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	@GetMapping(value = "/abonosregistrosactivos")
	public ResponseEntity<List<Abono>> listarAbonosyRegistrosActivos() {

		List<Abono> abonos = repAbono.findByEstado("A");
		for (Abono a : abonos) {
			//a.setRegistrosAbonos(repRegistroAbono.findByEstadoAndAbonoId("A", a.getId()));
			a.setRegistrosAbonos(repRegistroAbono.findByEstadoAndResultqr("A", a.getCodigoAbono()));
		}

		return new ResponseEntity<>(abonos, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Abono> recuperaAbono(@PathVariable("id") Long id) {

		Optional<Abono> abonoOpt = repAbono.findById(id);

		if (!abonoOpt.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		return new ResponseEntity<>(abonoOpt.get(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/codigo/{codigo}")
	public ResponseEntity<Abono> recuperaAbonoPorCodigo(@PathVariable("codigo") String codigo) {

		Optional<Abono> abonoOpt = repAbono.findByEstadoAndCodigoAbono("A",codigo);

		if (!abonoOpt.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		return new ResponseEntity<>(abonoOpt.get(), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/registrosabonossactivos")
	public ResponseEntity<Abono> recuperaAbonoWithRegistrosAbonosActivos(@PathVariable("id") Long id) {

		Optional<Abono> abonoOpt = repAbono.findByEstadoAndId("A", id);

		if (!abonoOpt.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		//abonoOpt.get().setRegistrosAbonos(repRegistroAbono.findByEstadoAndAbonoId("A", abonoOpt.get().getId()));
		abonoOpt.get().setRegistrosAbonos(repRegistroAbono.findByEstadoAndResultqr("A", abonoOpt.get().getCodigoAbono()));


		return new ResponseEntity<>(abonoOpt.get(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Abono> insertarUsuario(@RequestBody Abono usuario) {

		Abono usuarioNew = repAbono.save(usuario);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuarioNew.getId())
				.toUri();

		return ResponseEntity.created(location).body(usuarioNew);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Abono> actualizarUsuario(@PathVariable("id") Long id, @RequestBody Abono usuario) {

		Optional<Abono> usuarioOpt = repAbono.findById(id);
		if (!usuarioOpt.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		usuario.setId(usuarioOpt.get().getId());
		repAbono.save(usuario);

		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "duser/{id}")
	public ResponseEntity<Abono> desabilitarAbono(@PathVariable("id") Long id) {

		Optional<Abono> usuarioOpt = repAbono.findById(id);
		if (!usuarioOpt.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		usuarioOpt.get().setEstado("D");
		repAbono.save(usuarioOpt.get());

		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "auser/{id}")
	public ResponseEntity<Abono> habilitarAbono(@PathVariable("id") Long id) {

		Optional<Abono> abonoOpt = repAbono.findById(id);
		if (!abonoOpt.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		abonoOpt.get().setEstado("A");
		repAbono.save(abonoOpt.get());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> eliminarAbono(@PathVariable("id") Long id) {
		repAbono.deleteById(id);
		return new ResponseEntity<>("Abonado con ID :" + id + " eliminado correctamente", HttpStatus.OK);
	}

}
