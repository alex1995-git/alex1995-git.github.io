package com.depcue.rest;

import com.depcue.model.Asiento;
import com.depcue.model.Asiento;
import com.depcue.service.AsientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin()
@RequestMapping("/asientos")
public class RestAsientoController {

	@Autowired
	AsientoService asientoService;

	@GetMapping
	public ResponseEntity<List<Asiento>> listarAsientos() {
		List<Asiento> asientos = asientoService.getAsientos();
		return new ResponseEntity<>(asientos, HttpStatus.OK);
	}

	@GetMapping(value = "/activos")
	public ResponseEntity<List<Asiento>> listarqAsientosActivos() {
		List<Asiento> asientos = (List<Asiento>) asientoService.getAsientosActivos("A");
		return new ResponseEntity<>(asientos, HttpStatus.OK);
	}

	@GetMapping(value = "/asiento/{casiento}")
	public ResponseEntity<Asiento> qrbyId(@PathVariable("casiento") Long casiento) {
		Optional<Asiento> Asiento = asientoService.getAsiento(casiento);
		if (Asiento.isPresent()) {
			return ResponseEntity.ok().body(Asiento.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "bynumeroasiento", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Asiento> findAllByIde(
			@RequestParam(value = "numero", required = true) String numero) {
		Optional<Asiento> asiento = this.asientoService.getAsientobyNumero(numero);
		if (asiento.isPresent()) {
			return ResponseEntity.ok().body(asiento.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "bylocalidad", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<List<Asiento>> findByLocalidad(
			@RequestParam(value = "localidad", required = true) String localidad,
			@RequestParam(value = "estado", required = true, defaultValue = "A") String estado) {
		List<Asiento> asientos = this.asientoService.getByLocalidad(localidad, estado);
		return new ResponseEntity<>(asientos, HttpStatus.OK);
	}

	@PostMapping("/active")
	public ResponseEntity<Asiento> activar(@RequestParam(value = "casiento", required = true) Long casiento) {
		if (casiento == null) {
			return ResponseEntity.notFound().build();
		}
		Asiento asiento = asientoService.changeState(casiento, "A");
		if (asiento.getError()) {
			return ResponseEntity.ok().body(asiento);
		} else {
			return ResponseEntity.badRequest().body(asiento);
		}
	}

	@PostMapping("/block")
	public ResponseEntity<Asiento> bloquear(@RequestParam(value = "casiento", required = true) Long casiento) {
		if (casiento == null) {
			return ResponseEntity.notFound().build();
		}
		Asiento asiento = asientoService.changeState(casiento, "B");
		if (asiento.getError()) {
			return ResponseEntity.ok().body(asiento);
		} else {
			return ResponseEntity.badRequest().body(asiento);
		}
	}

}
