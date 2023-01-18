package com.depcue.rest;

import com.depcue.model.RegistroAbono;
import com.depcue.model.util.ResquestDashboard;
import com.depcue.model.util.ResultDasboard;
import com.depcue.service.BashBoardService;
import com.depcue.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
@RequestMapping("/dashboard")
public class RestDashboardController {

    @Autowired
    private BashBoardService bashBoardService;


    @PostMapping(value = "/allDashboard")
    public ResponseEntity<ResultDasboard> allDashboard(@RequestBody ResquestDashboard resquestDashboard) {
        ResultDasboard re = bashBoardService.allDasboard(resquestDashboard);
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
}
