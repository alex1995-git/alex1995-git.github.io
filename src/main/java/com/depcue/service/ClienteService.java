package com.depcue.service;

import com.depcue.model.Cliente;
import com.depcue.repository.IClienteRepository;
import com.depcue.util.DateUtil;
import com.depcue.util.StateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    public Optional<Cliente> getCliente(Long ccliente) {
        Optional<Cliente> cliente = clienteRepository.findById(ccliente);
        return cliente;
    }

    public Page<Cliente> getClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }


    public Page<Cliente> getClientesSearch(String value, Pageable pageable) {
        if (value == null) {
            value = "";
        }
        return clienteRepository.findBySearchClient(value.toUpperCase(), pageable);
    }

    //    public List<Cliente>getClientesActivos(String estado, Pageable pageable) {
//        List<Cliente> clientes =clienteRepository.findByEstado(estado, pageable);
//        return clientes;
//    }
    public Page<Cliente> getClientesActivos(String estado, Pageable pageable) {
        return clienteRepository.findByEstado(estado, pageable);

    }

    public Optional<Cliente> getClienteByIdentificacionEstado(String Identificacion, String estado) {
        Optional<Cliente> cliente = clienteRepository.findByIdentificacionAndEstado(Identificacion, estado);
        return cliente;
    }

    public Optional<Cliente> createClient(Cliente cliente) {

        try {

            Optional<Cliente> clientExist = clienteRepository.findByIdentificacionAndEstado(cliente.getIdentificacion(), "A");
            if (clientExist.isPresent()) {
                return Optional.of(new Cliente(true, "Cliente ya Existe"));
            }
            cliente.setEstado(StateUtil.STATE_ACTIVE);
            cliente.setFechaDesde(new Date());
            cliente.setFechaHasta(DateUtil.getDateMax());
            cliente = clienteRepository.save(cliente);

            Cliente clientNew = new Cliente();
            clientNew.setCcliente(cliente.getCcliente());

            return Optional.of(clientNew);
        } catch (Exception ex) {
            log.error("Error al crear Cliente " + cliente.getIdentificacion(), ex);
            return Optional.of(new Cliente(true, "Error al crear el Cliente"));
        }
    }

    public Cliente updateClient(Cliente client) {
        try {
            Optional<Cliente> clientExist = clienteRepository.findById(client.getCcliente());
            if (!clientExist.isPresent()) {
                client.setError(true);
                client.setMensaje("Error cliente a actualizar no existe");
                return client;
            }
            Cliente clientEdit;
            clientEdit = clientExist.get();
            clientEdit.setIdentificacion(client.getIdentificacion());
            clientEdit.setNombres(client.getNombres());
            clientEdit.setFechaNacimiento(client.getFechaNacimiento());
            clientEdit.setCorreo(client.getCorreo());
            clientEdit.setTelefono(client.getTelefono());
            clientEdit.setDireccion(client.getDireccion());
            clientEdit.setEstado(client.getEstado());
            clientEdit.setObservacion(client.getObservacion());
            return clienteRepository.save(clientEdit);
        } catch (Exception ex) {
            log.error("Error al actualizar cliente", ex);
            return new Cliente(true, "Error al actualizar cliente");
        }
    }

    public Cliente changeStateClient(Long ccliente, String estado) {
        try {
            Optional<Cliente> clientExist = clienteRepository.findById(ccliente);
            if (!clientExist.isPresent()) {
                log.error("Cliente " + ccliente + " a cambiar el estado no existe");
                return new Cliente(true, "Cliente a cambiar el estado no existe");
            }
            Cliente clientEdit;
            clientEdit = clientExist.get();
            clientEdit.setEstado(estado);
            if (estado.equals(StateUtil.STATE_CANCELED) || estado.equals(StateUtil.STATE_LOCKED))
                clientEdit.setFechaHasta(new Date());
            return clienteRepository.save(clientEdit);
        } catch (Exception ex) {
            log.error("Error al cambiar el estado del cliente ", ex);
            return new Cliente(true, "Error al cambiar el estado del cliente");
        }
    }


}
