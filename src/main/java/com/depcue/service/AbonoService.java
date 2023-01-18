package com.depcue.service;

import com.depcue.model.Abono;
import com.depcue.model.Asiento;
import com.depcue.model.QRCode;
import com.depcue.model.Suscripcion;
import com.depcue.repository.IAbonoRepository;
import com.depcue.repository.IAsientoRepository;
import com.depcue.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AbonoService {

    @Autowired
    private IAbonoRepository abonoRepository;
    @Autowired
    private IAsientoRepository asientoRepository;
    @Autowired
    private QRCodeService qrCodeService;

    public Optional<Abono> getAbono(Long cabono) {
        Optional<Abono> abono = abonoRepository.findById(cabono);
        return abono;
    }

    public List<Abono> getAbonos() {
        List<Abono> items = abonoRepository.findAll();
        return items;
    }

    public List<Abono> getAbonosActivos(String estado) {
        List<Abono> abonos = abonoRepository.findByEstado(estado);
        return abonos;
    }

    public List<Abono> getAbonoBySuscripcion(Long suscripcion) {
        List<Abono> abonos = abonoRepository.findByCsuscripcion(new Suscripcion(suscripcion));
        return abonos;
    }

    public Optional<Abono> create(Abono abono) {

        try {

            Optional<Abono> abonoexist = abonoRepository.findById(abono.getId());
            if (abonoexist.isPresent()) {
                return Optional.of(new Abono(true, "abono ya Existe"));
            }

            abono.setEstado("A");
            Long cqrcode = abono.getCqrcode().getCqrcode();
            Optional<Asiento> asiento = asientoRepository.findById(abono.getCasiento().getCasiento());
            if (!asiento.isPresent()) {
                log.error("Asiento no encontrado con el ID " + abono.getCasiento().getCasiento());
            }
            if (abono.isQrAutomatico()) {
                cqrcode = qrCodeService.createQRCodeAutomatico(asiento.get().getCodLocalidad());
            }

            abono.setFechaDesde(new Date());
            abono.setFechaHasta(DateUtil.getDateMax());
            abono.setCqrcode(new QRCode(cqrcode));
            abono = abonoRepository.save(abono);


            return Optional.of(new Abono(abono.getId()));
        } catch (Exception ex) {
            log.error("Error al crear abono ", ex);
            return Optional.of(new Abono(true, "Error al crear el abono"));
        }
    }

    public Abono changeState(Long cabono, String estado) {
        try {
            Optional<Abono> abonoxist = abonoRepository.findById(cabono);
            if (!abonoxist.isPresent()) {
                log.error("abono " + cabono + " a cambiar el estado no existe");
                return new Abono(true, "abono a cambiar el estado no existe");
            }
            Abono abonoedit = abonoxist.get();
            abonoedit.setEstado(estado);

            if (estado.equals("C") || estado.equals("B"))
                abonoedit.setFechaHasta(new Date());
            return abonoRepository.save(abonoedit);
        } catch (Exception ex) {
            log.error("Error al cambiar el estado del abono ", ex);
            return new Abono(true, "Error al cambiar el estado del abono");
        }
    }


}
