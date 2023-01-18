package com.depcue.service;

import com.depcue.model.Asiento;
import com.depcue.repository.IAsientoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AsientoService {
    
    @Autowired
    private IAsientoRepository asientoRepository;

    public List<Asiento> getAsientos() {
        List<Asiento> Asientos =asientoRepository.findAll();
        return Asientos;
    }

    public List<Asiento>getAsientosActivos(String estado) {
        List<Asiento> Asientos =asientoRepository.findByEstado(estado);
        return Asientos;
    }

    public List<Asiento>getByLocalidad(String localidad,String estado) {
        List<Asiento> Asientos =asientoRepository.findByLocalidadAndEstado(localidad,estado);
        return Asientos;
    }

    public Optional<Asiento> getAsiento(Long casiento) {
        Optional<Asiento> asiento = asientoRepository.findById(casiento);
        return asiento;
    }

    public Optional<Asiento> getAsientobyNumero(String numero) {
        Optional<Asiento> asiento = asientoRepository.findByAsiento(numero);
        return asiento;
    }

    public Asiento changeState(Long casiento, String estado) {
        try {
            Optional<Asiento> asientoExist = asientoRepository.findById(casiento);
            if (!asientoExist.isPresent()) {
                return new Asiento(true, "Asiento no existe");
            }
            Asiento asientoEdit;
            asientoEdit = asientoExist.get();
            asientoEdit.setEstado(estado);
            return asientoRepository.save(asientoEdit);
        } catch (Exception ex) {
            log.error("Error al cambiar estado de Asiento",ex);
            return new Asiento(true, "Error al cambiar estado de Asiento");
        }
    }


    
}
