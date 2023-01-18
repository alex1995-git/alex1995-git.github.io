package com.depcue.rest;

import com.depcue.model.QRCode;
import com.depcue.service.QRCodeService;
import com.depcue.util.StateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin()
@RequestMapping("/qrcode")
public class RestQRCodeController {

	@Autowired
	QRCodeService qrcodeService;

	@GetMapping
	public ResponseEntity<List<QRCode>> listarQRCodes() {
		List<QRCode> qrCodes = qrcodeService.getQRCodes();
		return new ResponseEntity<>(qrCodes, HttpStatus.OK);
	}
	
	@GetMapping(value = "/activos")
	public ResponseEntity<List<QRCode>> listarqrCodesActivos() {
		List<QRCode> qrCodes = (List<QRCode>) qrcodeService.getQRCodesActivos("A");
		return new ResponseEntity<>(qrCodes, HttpStatus.OK);
	}

	@GetMapping(value = "/qrcode/{cqrcode}")
	public ResponseEntity<QRCode> qrbyId(@PathVariable("cqrcode") Long cqrcode) {
		Optional<QRCode> qrCode = qrcodeService.getQRCode(cqrcode);
		if (qrCode.isPresent()) {
			return ResponseEntity.ok().body(qrCode.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "bycodenumber", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<QRCode>  findAllByIde(
			@RequestParam(value = "code", required = true) String code) {
		Optional<QRCode> qrCode = this.qrcodeService.getQRCodebyCodigo(code);
		if (qrCode.isPresent()) {
			return ResponseEntity.ok().body(qrCode.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/active")
	public ResponseEntity<QRCode> activar(@RequestParam(value = "cqrcode", required = true) Long cqrcode) {
		if (cqrcode == null) {
			return ResponseEntity.notFound().build();
		}
		QRCode qrCode = qrcodeService.changeState(cqrcode, StateUtil.STATE_ACTIVE);
		if (qrCode.getError()) {
			return ResponseEntity.ok().body(qrCode);
		} else {
			return ResponseEntity.badRequest().body(qrCode);
		}
	}

	@PostMapping("/block")
	public ResponseEntity<QRCode> bloquear(@RequestParam(value = "cqrcode", required = true) Long cqrcode) {
		if (cqrcode == null) {
			return ResponseEntity.notFound().build();
		}
		QRCode qrCode = qrcodeService.changeState(cqrcode,StateUtil.STATE_LOCKED);
		if (qrCode.getError()) {
			return ResponseEntity.ok().body(qrCode);
		} else {
			return ResponseEntity.badRequest().body(qrCode);
		}
	}



}
