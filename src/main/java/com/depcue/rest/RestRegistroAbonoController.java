package com.depcue.rest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.depcue.util.StateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depcue.model.Abono;
import com.depcue.model.RegistroAbono;

import com.depcue.repository.IAbonoRepository;
import com.depcue.repository.IRegistroAbonoRepository;
import com.depcue.scheduletask.ScheduleTask;

@RestController
@CrossOrigin()
@RequestMapping("/registrosabonos")
public class RestRegistroAbonoController {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduleTask.class);
	private final IAbonoRepository repAbono;
	private final IRegistroAbonoRepository repRegistroAbono;

	@Autowired
	public RestRegistroAbonoController(IAbonoRepository repAbono, IRegistroAbonoRepository repRegistroAbonoRepository) {
		this.repAbono = repAbono;
		this.repRegistroAbono = repRegistroAbonoRepository;
	}

	@GetMapping
	public List<RegistroAbono> listarRegistrosAbonos() {
		return repRegistroAbono.findAll();
	}

	@GetMapping(value = "/registrosabonosactivos")
	public ResponseEntity<List<RegistroAbono>> listarRegistrosAbonosActivos() {
		List<RegistroAbono> list = repRegistroAbono.findByEstado("A");
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/registrosabonosactivos/{id}")
	public ResponseEntity<List<RegistroAbono>> listarRegistrosAbonosActivos(@PathVariable Long id) {
		Optional<Abono> abono= repAbono.findById(Long.valueOf(id));
		//List<RegistroAbono> list = repRegistroAbono.findByEstadoAndAbonoId("A", Long.valueOf(id));
		List<RegistroAbono> list = repRegistroAbono.findByEstadoAndResultqr("A", abono.get().getCodigoAbono());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RegistroAbono> recuperaRegistroAbono(@PathVariable Long id) {
		Optional<RegistroAbono> regAbon = repRegistroAbono.findById(id);
		if (!regAbon.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		return ResponseEntity.ok(regAbon.get());
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<RegistroAbono> actualizarRegistro(@PathVariable("id") Long id,
			@RequestBody RegistroAbono registro) {

		//Optional<Abono> usuarioOpt = repAbono.findById(registro.getAbono().getId());
		Optional<Abono> usuarioOpt = repAbono.findByEstadoAndCodigoAbono(StateUtil.STATE_ACTIVE,registro.getResultqr());
		if (!usuarioOpt.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		Optional<RegistroAbono> registroOpt = repRegistroAbono.findById(id);
		if (!registroOpt.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		//registro.setAbono(usuarioOpt.get());
		registro.setQr_abono(usuarioOpt.get().getCodigoAbono());
		registro.setId(registroOpt.get().getId());
		repRegistroAbono.save(registro);

		return ResponseEntity.noContent().build();

	}

	@PutMapping(value = "dregistroabono/{id}")
	public ResponseEntity<Abono> desabilitarRegistroAbono(@PathVariable("id") Long id) {

		Optional<RegistroAbono> regAbon = repRegistroAbono.findById(id);
		if (!regAbon.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		regAbon.get().setEstado("D");
		repRegistroAbono.save(regAbon.get());

		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "aregistroabono/{id}")
	public ResponseEntity<Abono> habilitarRegistroAbono(@PathVariable("id") Long id) {

		Optional<RegistroAbono> regAbon = repRegistroAbono.findById(id);
		if (!regAbon.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		regAbon.get().setEstado("A");
		repRegistroAbono.save(regAbon.get());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public void eliminarRegistroAbono(@PathVariable("id") Long id) {
		repRegistroAbono.deleteById(id);
	}

	@PostMapping(value = "/registroabonovalido")
	public ResponseEntity<Object> agregarRegistroAbonoValido(@RequestBody RegistroAbono registroAbono) {

		try {

			String codigoAbono = "";
			String htmlSource = "";
			String resultqr = registroAbono.getResultqr();
			String mensajeResponse = "";

			Optional<Abono> abonoOpt = repAbono.findByEstadoAndCodigoAbono("A", resultqr);

			if (!abonoOpt.isPresent()) {

				abonoOpt = repAbono.findByEstadoAndCodigoAbono("B", resultqr);
				if (!abonoOpt.isPresent()) {

					mensajeResponse = "ABONO NO SE ENCUENTRA REGISTRADO!";
					return ResponseHandler.generateResponse(mensajeResponse, HttpStatus.LOCKED, null);
				} else {
					mensajeResponse = "ABONO SE ENCUENTRA BLOQUEADO!";
					return ResponseHandler.generateResponse(mensajeResponse, HttpStatus.LOCKED, null);
				}
			}

			Abono abono = abonoOpt.get();

			List<RegistroAbono> listRegsAbonos = repRegistroAbono.findByEstadoAndResultqr("A", resultqr);
			if (listRegsAbonos.size() > 0) {
				mensajeResponse = "ABONADO YA SE ENCUENTRA INGRESADO, VERIFIQUE!";
				abono.setObservacion(mensajeResponse);
				LOG.info("Mensaje: " + HttpStatus.ALREADY_REPORTED);
				return ResponseHandler.generateResponse(mensajeResponse, HttpStatus.ALREADY_REPORTED, abono);

			} else {
				mensajeResponse = "ABONADO INGRESADO CORRECTAMENTE!";
				RegistroAbono regabo = new RegistroAbono(resultqr);
				//regabo.setAbono(abono);
				regabo.setQr_abono(abono.getCodigoAbono());
				RegistroAbono resultRegistroAbono = repRegistroAbono.save(regabo);
				List<RegistroAbono> list = new LinkedList<RegistroAbono>();
				list.add(resultRegistroAbono);
				abono.setRegistrosAbonos(list);
				abono.setObservacion(mensajeResponse);
				return ResponseHandler.generateResponse(mensajeResponse, HttpStatus.OK, abono);
			}
		} catch (Exception e) {
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
		}
	}

}
