package com.depcue.service;

import com.depcue.model.QRCode;
import com.depcue.repository.IQRCodeRepository;
import com.depcue.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QRCodeService {

    @Autowired
    private IQRCodeRepository qrcodeRepository;


    public List<QRCode> getQRCodes() {
        List<QRCode> qrCodes =qrcodeRepository.findAll();
        return qrCodes;
    }

    public List<QRCode>getQRCodesActivos(String estado) {
        List<QRCode> qrCodes =qrcodeRepository.findByEstado(estado);
        return qrCodes;
    }

    public Optional<QRCode> getQRCode(Long cqrcode) {
        Optional<QRCode> qrCode = qrcodeRepository.findById(cqrcode);
        return qrCode;
    }

    public Optional<QRCode> getQRCodebyCodigo(String codigoqr) {
        Optional<QRCode> qrCode = qrcodeRepository.findByCodigo(codigoqr);
        return qrCode;
    }

    public Long createQRCodeAutomatico(String alias){
        try{
            Long maxCode = qrcodeRepository.findMaxCode(alias);
            String newCode = "";
            if (maxCode!=null) {
                newCode = String.format("%03d",maxCode + 1);
            }else {
                newCode = String.format("%03d",1);;
            }

            QRCode qrcode = new QRCode();
            qrcode.setCodLoc(alias);
            qrcode.setSerie(newCode);
            qrcode.setCodigo(alias+"-"+newCode);
            qrcode.setEstado("A");
            qrcode.setFechaDesde(new Date());
            qrcode.setFechaHasta(DateUtil.getDateMax());

            qrcode = qrcodeRepository.save(qrcode);
            return qrcode.getCqrcode();
        } catch (Exception ex) {
            log.error("Error al cambiar el estado del suscripcion ",ex);
            return null;
        }
    }

    public QRCode changeState(Long qrCode, String estado) {
        try {
            Optional<QRCode> qrCodeExist = qrcodeRepository.findById(qrCode);
            if (!qrCodeExist.isPresent()) {
                return new QRCode(true, "QRCode no existe");
            }
            QRCode qrCodeEdit;
            qrCodeEdit = qrCodeExist.get();
            qrCodeEdit.setEstado(estado);
            return qrcodeRepository.save(qrCodeEdit);
        } catch (Exception ex) {
            log.error("Error al cambiar estado de QRCode",ex);
            return new QRCode(true, "Error al cambiar estado de QRCode");
        }
    }
    
}
