package com.depcue.service;

import com.depcue.model.*;
import com.depcue.model.util.AbonoLst;
import com.depcue.repository.IAbonoRepository;
import com.depcue.repository.IAsientoRepository;
import com.depcue.repository.IQRCodeRepository;
import com.depcue.repository.ISuscripcionRepository;
import com.depcue.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SuscripcionService {

    @Autowired
    private ISuscripcionRepository subscripcionRepository;

    @Autowired
    private IAbonoRepository abonoRepository;
    @Autowired
    private IAsientoRepository asientoRepository;
    @Autowired
    private QRCodeService qrCodeService;

    public Optional<Suscripcion> getSuscripcion(Long csubscripcion) {
        Optional<Suscripcion> subscripcion = subscripcionRepository.findById(csubscripcion);
        return subscripcion;
    }
    public Page<Suscripcion> getSuscripciones(Integer page, Integer size) {
        return subscripcionRepository.findAll(PageRequest.of(page, size));

    }

    public Page<Suscripcion> getSuscripcionesActivos(String estado, Integer page, Integer size) {
        return subscripcionRepository.findByEstado(estado, PageRequest.of(page, size));
    }

    public List<Suscripcion> getSubscripcionByClient(Long cclient) {
        List<Suscripcion> suscripcions = subscripcionRepository.findByCcliente( new Cliente(cclient));
        return suscripcions;
    }

    public Optional<Suscripcion> create(Suscripcion suscripcion) {

        try {

            Optional<Suscripcion> subscripcionxist = subscripcionRepository.findById(suscripcion.getCsuscripcion());
            if (subscripcionxist.isPresent()) {
                return  Optional.of( new Suscripcion(true, "suscripcion ya Existe"));
            }
            Suscripcion newSuscripcion = subscripcionRepository.save(suscripcion);

            //detalle suscripcion
            if (!(suscripcion.getListAbonos() == null || suscripcion.getListAbonos().isEmpty())) {
                List<Abono> items = new ArrayList<>();
                suscripcion.getListAbonos().forEach((AbonoLst item) -> {
                    Abono detail = new Abono();
                    detail.setCsuscripcion(new Suscripcion(newSuscripcion.getCsuscripcion()));
                    detail.setEstado("A");
                    detail.setObservacion(item.getObservacion());
                    detail.setValorAbono(item.getValorAbono());
                    Long cqrcode = item.getCqrcode();

                    Optional<Asiento> asiento = asientoRepository.findById(item.getAsiento());
                    if(!asiento.isPresent()){
                       log.error("Asiento no encontrado con el ID " + item.getAsiento().toString());
                    }
                    if(item.isQrAutomatico()){
                        cqrcode = qrCodeService.createQRCodeAutomatico(asiento.get().getCodLocalidad());
                    }
                    detail.setFechaDesde(new Date());
                    detail.setFechaHasta(DateUtil.getDateMax());
                    detail.setCasiento(asiento.get());
                    detail.setCqrcode(new QRCode(cqrcode));
                    items.add(detail);

                });
                abonoRepository.saveAll(items);
            }

            return  Optional.of(new Suscripcion(newSuscripcion.getCsuscripcion()));
        } catch (Exception ex) {
            log.error("Error al crear suscripcion ",ex);
            return  Optional.of( new Suscripcion(true, "Error al crear el suscripcion"));
        }
    }


    public Suscripcion changeState(Long csuscripcion, String estado) {
        try {
            Optional<Suscripcion> subscripcionxist = subscripcionRepository.findById(csuscripcion);
            if (!subscripcionxist.isPresent()) {
                log.error("suscripcion "+csuscripcion+" a cambiar el estado no existe");
                return new Suscripcion(true, "suscripcion a cambiar el estado no existe");
            }
            Suscripcion subscripciondit;
            subscripciondit = subscripcionxist.get();
            subscripciondit.setEstado(estado);
            if(estado.equals("C")||estado.equals("B"))
                subscripciondit.setFechaHasta(new Date());
            return subscripcionRepository.save(subscripciondit);
        } catch (Exception ex) {
            log.error("Error al cambiar el estado del suscripcion ",ex);
            return new Suscripcion(true, "Error al cambiar el estado del suscripcion");
        }
    }





}
