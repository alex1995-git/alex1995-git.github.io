
package com.depcue.rest;

import com.depcue.model.AbonadosMasive;
import com.depcue.model.util.ResponseGeneric;
import com.depcue.model.util.ResultCargarCab;
import com.depcue.service.CargaMasivaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/cargaMasiva")
public class RestCargaMasiva {

    @Autowired
    private CargaMasivaService cargaMasivaService;

    @PostMapping("/consolidado")
    public ResponseEntity<ResponseGeneric> cargaMasiveConsolidado(@RequestParam("file") MultipartFile file) {
        ResponseGeneric responseGeneric = cargaMasivaService.processCargaMasiveConsolidado("CONSOLIDADO", file);
        if (responseGeneric.isError()) {
            return ResponseEntity.badRequest().body(responseGeneric);
        }
        return ResponseEntity.ok().body(responseGeneric);
    }


    @GetMapping(value = "/resultCarga")
    public ResponseEntity<List<ResultCargarCab>> resultCarga() {
        List<ResultCargarCab> resultCargarCabs = cargaMasivaService.resultCarga();
        return new ResponseEntity<>(resultCargarCabs, HttpStatus.OK);
    }

    @RequestMapping(value = "/resultCargaDetalle", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<AbonadosMasive>> resultCargaDetalle(
            @RequestParam(value = "codigoUnico", required = true) String codigoUnico) {
        List<AbonadosMasive> resultCargaDetalle = this.cargaMasivaService.resultCargaDetalle(codigoUnico);
        return new ResponseEntity<>(resultCargaDetalle, HttpStatus.OK);
    }


}
