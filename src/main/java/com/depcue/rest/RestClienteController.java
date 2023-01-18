package com.depcue.rest;

import com.depcue.model.Cliente;
import com.depcue.service.ClienteService;
import com.depcue.util.ParamUtil;
import com.depcue.util.StateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin()
@RequestMapping("/clients")
public class RestClienteController {

	@Autowired
	ClienteService clienteService;


	@GetMapping(value = "/all")
	public Page<Cliente> getclients(
			@RequestParam(value = "page", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_NUM) Integer page,
			@RequestParam(value = "size", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_SIZE) Integer size) {
		return clienteService.getClientes(PageRequest.of(page, size));
	}

	//@GetMapping(value = "/actives")
//	public ResponseEntity<List<Cliente>> getActives() {
//		List<Cliente> clientes = (List<Cliente>) clienteService.getClientesActivos("A");
//		return new ResponseEntity<>(clientes, HttpStatus.OK);
//	}

	@GetMapping(value = "/actives")
	public @ResponseBody Page<Cliente> getActives(
			@RequestParam(value = "page", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_NUM) Integer page,
			@RequestParam(value = "size", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_SIZE) Integer size) {
			return clienteService.getClientesActivos(StateUtil.STATE_ACTIVE, PageRequest.of(page, size));
	}

	@RequestMapping(value = "byid", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Cliente>  findAllById(
			@RequestParam(value = "ccliente", required = true) Long ccliente) {
		Optional<Cliente> client = this.clienteService.getCliente(ccliente);
		if (client.isPresent()) {
			return ResponseEntity.ok().body(client.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@RequestMapping(value = "bycedula", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Cliente>  findAllByCedula(
			@RequestParam(value = "cedula", required = true) String cedula,
			@RequestParam(value = "estado", required = true,defaultValue = "A") String estado) {
		Optional<Cliente> client = this.clienteService.getClienteByIdentificacionEstado(cedula,estado);
		if (client.isPresent()) {
			return ResponseEntity.ok().body(client.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/save")
	public ResponseEntity<Cliente> saveClient(@Validated @RequestBody Cliente client) {
		if (client == null) {
			return ResponseEntity.notFound().build();
		}
		Optional<Cliente> clienteNew = clienteService.createClient(client);
		if (clienteNew.get().getError()) {
			return ResponseEntity.ok().body(clienteNew.get());
		} else {
			return ResponseEntity.badRequest().body(clienteNew.get());
		}
	}


	@PostMapping("/update")
	public ResponseEntity<Cliente> updateClient(@Validated @RequestBody Cliente client) {
		if (client == null) {
			return ResponseEntity.notFound().build();
		}
		client = clienteService.updateClient(client);
		if (!client.getError()) {
			return ResponseEntity.ok().body(client);
		} else {
			return ResponseEntity.badRequest().body(client);
		}
	}

	@PostMapping("/active")
	public ResponseEntity<Cliente> activeClient(@RequestParam(value = "ccliente", required = true) Long ccliente) {
		if (ccliente == null) {
			return ResponseEntity.notFound().build();
		}
		Cliente client = clienteService.changeStateClient(ccliente, StateUtil.STATE_ACTIVE);
		if (!client.getError()) {
			return ResponseEntity.ok().body(client);
		} else {
			return ResponseEntity.badRequest().body(client);
		}
	}

	@PostMapping("/cancel")
	public ResponseEntity<Cliente> cancelClient(@RequestParam(value = "ccliente", required = true) Long ccliente) {
		if (ccliente == null) {
			return ResponseEntity.notFound().build();
		}
		Cliente client = clienteService.changeStateClient(ccliente, StateUtil.STATE_CANCELED);
		if (!client.getError()) {
			return ResponseEntity.ok().body(client);
		} else {
			return ResponseEntity.badRequest().body(client);
		}
	}

	@GetMapping(value = "/search")
	public @ResponseBody Page<Cliente> search(
			@RequestParam(value = "page", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_NUM) Integer page,
			@RequestParam(value = "size", required = true, defaultValue = ParamUtil.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(value = "value", required = true) String value) {
		return clienteService.getClientesSearch(value,PageRequest.of(page, size));
	}

}
