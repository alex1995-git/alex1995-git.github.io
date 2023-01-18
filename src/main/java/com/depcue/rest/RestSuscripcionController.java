package com.depcue.rest;

import com.depcue.model.Suscripcion;
import com.depcue.service.SuscripcionService;
import com.depcue.util.ParamUtil;
import com.depcue.util.StateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin()
@RequestMapping("/suscripcion")
public class RestSuscripcionController {



	@Autowired
	SuscripcionService subService;

	@GetMapping(value = "/all")
	public Page<Suscripcion> getSuscripciones(
			@RequestParam(value = "page", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_NUM) Integer page,
			@RequestParam(value = "size", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_SIZE) Integer size) {
		return subService.getSuscripciones(page, size);

	}

	@GetMapping(value = "/actives")
	public Page<Suscripcion> getActives(
			@RequestParam(value = "page", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_NUM) Integer page,
			@RequestParam(value = "size", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_SIZE) Integer size) {
		return subService.getSuscripcionesActivos(StateUtil.STATE_ACTIVE, page, size);
	}

	@RequestMapping(value = "byid", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Suscripcion>  findById(
			@RequestParam(value = "csuscripcion", required = true) Long csuscripcion) {
		Optional<Suscripcion> Suscripcion = this.subService.getSuscripcion(csuscripcion);
		if (Suscripcion.isPresent()) {
			return ResponseEntity.ok().body(Suscripcion.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@RequestMapping(value = "byclient", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<List<Suscripcion>>  findAllByClient(
			@RequestParam(value = "cclient", required = true) Long cclient) {
		List<Suscripcion> items = this.subService.getSubscripcionByClient(cclient);
		return new ResponseEntity<>(items, HttpStatus.OK);
	}


	@PutMapping("/save")
	public ResponseEntity<Suscripcion> saveSuscripcion(@Validated @RequestBody Suscripcion Suscripcion) {
		if (Suscripcion == null) {
			return ResponseEntity.notFound().build();
		}
		Optional<Suscripcion> suscripcionNew = subService.create(Suscripcion);
		if (suscripcionNew.get().getError()) {
			return ResponseEntity.ok().body(suscripcionNew.get());
		} else {
			return ResponseEntity.badRequest().body(suscripcionNew.get());
		}
	}


	@PostMapping("/active")
	public ResponseEntity<Suscripcion> activeSuscripcion(@RequestParam(value = "csuscripcion", required = true) Long csuscripcion) {
		if (csuscripcion == null) {
			return ResponseEntity.notFound().build();
		}
		Suscripcion Suscripcion = subService.changeState(csuscripcion, "A");
		if (!Suscripcion.getError()) {
			return ResponseEntity.ok().body(Suscripcion);
		} else {
			return ResponseEntity.badRequest().body(Suscripcion);
		}
	}

	@PostMapping("/cancel")
	public ResponseEntity<Suscripcion> cancelSuscripcion(@RequestParam(value = "csuscripcion", required = true) Long csuscripcion) {
		if (csuscripcion == null) {
			return ResponseEntity.notFound().build();
		}
		Suscripcion suscripcion = subService.changeState(csuscripcion, "C");
		if (!suscripcion.getError()) {
			return ResponseEntity.ok().body(suscripcion);
		} else {
			return ResponseEntity.badRequest().body(suscripcion);
		}
	}


}
