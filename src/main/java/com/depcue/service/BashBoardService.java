package com.depcue.service;

import com.depcue.model.util.ResquestDashboard;
import com.depcue.model.util.ResultDasboard;
import com.depcue.repository.IBashBoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BashBoardService {

    @Autowired
    private IBashBoardRepository iBashBoardRepository;


    public ResultDasboard allDasboard(ResquestDashboard resquestDashboard) {

        try {
            Integer totalAbonados = iBashBoardRepository.findTotalAbonados();
            Integer totalSubcripciones = iBashBoardRepository.findTotalAbonados();
            BigDecimal toalVentas = iBashBoardRepository.findTotalVentas();
           //sets variables
            ResultDasboard resultDasboard = new ResultDasboard();
            resultDasboard.setTotalAbonados(totalAbonados);
            resultDasboard.setTotalVentas(toalVentas);
            resultDasboard.setTotalSusCripciones(totalSubcripciones);
            return resultDasboard;
        } catch (Exception e) {
            log.error("Error occurred", e);
            return new ResultDasboard(true, "msg_error_recuperar_dashboard");
        }
    }

}
