package com.depcue.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

import com.depcue.model.*;
import com.depcue.model.util.ResultCargarCab;
import com.depcue.repository.*;
import com.depcue.util.DateUtil;
import com.depcue.util.StateUtil;
import org.springframework.web.multipart.MultipartFile;

import com.depcue.model.util.ResponseGeneric;
import com.depcue.util.UtilCargaMasiva;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CargaMasivaService {


    @Autowired
    private IAbonadosMasiveRepository iAbonadosMasiveRepository;
    @Autowired
    private IQRCodeRepository iqrCodeRepository;
    @Autowired
    private IAsientoRepository iAsientoRepository;
    @Autowired
    private IClienteRepository iClienteRepository;
    @Autowired
    private ISuscripcionRepository iSuscripcionRepository;
    @Autowired
    private IAbonoRepository iAbonoRepository;


    public List<AbonadosMasive> resultCargaDetalle(String codigo) {
        try {
            List<AbonadosMasive> list = iAbonadosMasiveRepository.findResultByCode(codigo);

            return list;
        } catch (Exception e) {
            log.error("Error occurred", e);
            return Collections.EMPTY_LIST;
        }
    }

    public List<ResultCargarCab> resultCarga() {
        try {
            List list = iAbonadosMasiveRepository.findResultAll();
            if (list.isEmpty()) {
                return Collections.EMPTY_LIST;
            }
            List<ResultCargarCab> listfinal = new ArrayList<>();
            for (Object obj : list) {
                Object[] array = (Object[]) obj;
                ResultCargarCab une = new ResultCargarCab();
                une.setCodigoUnico(array[0].toString());
                une.setFechaRegistro((Date) array[1]);
                une.setUsernameCarga(array[2].toString());
                une.setTotalRegistros((BigInteger) array[3]);
                listfinal.add(une);
            }

            return listfinal;
        } catch (Exception e) {
            log.error("Error occurred", e);
            return Collections.EMPTY_LIST;
        }
    }


    public ResponseGeneric processCargaMasiveConsolidado(String tipo, MultipartFile file) {
        InputStream archivo;
        try {
            archivo = file.getInputStream();
        } catch (IOException e) {
            log.error("Error occurred", e);
            return new ResponseGeneric(true, "msg_error_al_obtener_archivo");
        }
        if (archivo == null) {
            return new ResponseGeneric(true, "msg_no_existe_archivo");
        }
        // primero leo el excel y cargo a una lista
        try {

            UtilCargaMasiva cargaMasiva = new UtilCargaMasiva();
            ResponseGeneric resp = cargaMasiva.execute(archivo);
            if (resp.isError()) {
                return resp;
            }
            List<AbonadosMasive> abonados = (List<AbonadosMasive>) resp.getData();
            if (abonados == null || abonados.isEmpty()) {
                return new ResponseGeneric(true, "msg_no_existe_informacion_cargar");
            }
            iAbonadosMasiveRepository.saveAll(abonados);
            String guiMigracion = resp.getMessage();
            // pendiente la fase 2
            int valida = iAbonadosMasiveRepository.procesarMigracionConsolidado(guiMigracion);

            boolean res = uploadFileComplete(guiMigracion);

            if (valida == 1 && res) {
                log.error("Archivo cargado con observaciones");
                return new ResponseGeneric(false, "msg_proceso_con_observacion");
            }
            if (res)
                return new ResponseGeneric(false, "msg_proceso_completado");

            return new ResponseGeneric(false, "msg_proceso_no_completado");
        } catch (Exception e) {
            log.error("Error occurred", e);
            return new ResponseGeneric(true, "msg_error_migracion");
        }
    }

    private boolean uploadFileComplete(String codeUnico) {

        List<AbonadosMasive> listabonados = iAbonadosMasiveRepository.findResultByCode(codeUnico);
        if (listabonados.isEmpty()) {
            log.error("No existe Abonados masivos");
            return false;
        }

        try {

            for (AbonadosMasive abonadosMasive : listabonados) {
                if (abonadosMasive.getCedula() != null && !abonadosMasive.getCedula().trim().isEmpty()) {
                    Optional<Cliente> cliente = iClienteRepository.findByIdentificacionAndEstado(abonadosMasive.getCedula().trim(), StateUtil.STATE_ACTIVE);
                    if (cliente.isPresent()) {
                        persistenceClient(abonadosMasive, cliente.get(), false);
                    } else {
                        persistenceClient(abonadosMasive, null, true);
                    }
                }

            }

            List<Cliente> listCliente = iClienteRepository.findByEstadoAndCarga(StateUtil.STATE_ACTIVE);

            for (Cliente cliente : listCliente) {

                Optional<Suscripcion> suscripcion = iSuscripcionRepository.findByCclienteAndEstado(cliente, StateUtil.STATE_ACTIVE);
                if (suscripcion.isPresent()) {
                    persistenceSuscripcion(codeUnico, cliente, suscripcion.get(), false);
                } else {
                    persistenceSuscripcion(codeUnico, cliente, null, true);
                }
            }
        } catch (Exception ex) {
            log.error("Error al completar la carga masiva de abonados : " + ex.toString());
            return false;
        }
        return true;
    }

    private Cliente persistenceClient(AbonadosMasive abonadosMasive, Cliente clienteEdit, boolean insert) {

        try {
            Cliente cliente = new Cliente();
            if (!insert)
                cliente = clienteEdit;

            cliente.setIdentificacion(abonadosMasive.getCedula());
            cliente.setNombres(abonadosMasive.getNombres() +" "+ abonadosMasive.getApellidos());
            cliente.setTelefono(abonadosMasive.getTelefono());
            cliente.setCorreo(abonadosMasive.getCorreo());
            cliente.setEstado(StateUtil.STATE_ACTIVE);
            cliente.setFechaDesde(new Date());
            cliente.setFechaHasta(DateUtil.getDateMax());
            return iClienteRepository.save(cliente);
        } catch (Exception ex) {
            log.error("Error al registrar el cliente con cedula " + abonadosMasive.getCedula() + " error: " + ex.toString());
            return null;
        }

    }

    private Long persistenceSuscripcion(String codeUnico, Cliente cliente, Suscripcion suscripcionEdit, boolean insert) {
        int numAbonos = iAbonadosMasiveRepository.findNumByCodeUnicoAndCedula(codeUnico, cliente.getIdentificacion());
        double sumAbonos = iAbonadosMasiveRepository.findNumByCodeUnicoAndCedula(codeUnico, cliente.getIdentificacion());

        List<AbonadosMasive> abonadosMasives = iAbonadosMasiveRepository.findByCodeUnicoAndCedula(codeUnico, cliente.getIdentificacion());

        if (cliente.getIdentificacion().equals("0103994554")) {
            System.out.println("hello");
        }

        if (numAbonos == 0) {
            log.error("cliente: " + cliente.getNombres() + " No existen abonos detalle a registrar");
            return null;
        }

       /* if(sumAbonos == 0){
            log.error("cliente: "+cliente.getNombres()+" No existen monto total a registrar");
            return null;
        }*/

        Suscripcion suscripcion = new Suscripcion();
        if (!insert) {
            suscripcion = suscripcionEdit;
        }

        suscripcion.setEstado(StateUtil.STATE_ACTIVE);
        suscripcion.setCcliente(cliente);
        suscripcion.setFechaDesde(new Date());
        suscripcion.setFechaHasta(DateUtil.getDateMax());
        suscripcion.setCantidad(numAbonos);
        suscripcion.setValor(sumAbonos);
        suscripcion = iSuscripcionRepository.save(suscripcion);

        List<Abono> abonos = new ArrayList<>();
        numAbonos = 0;
        sumAbonos = 0;
        for (AbonadosMasive abonadoMasive : abonadosMasives) {
            boolean error = false;
            String msg = null;
            try {
                Optional<QRCode> qrCode = iqrCodeRepository.findByCodigoAndEstado(abonadoMasive.getQr(), StateUtil.STATE_ACTIVE);

                if (!qrCode.isPresent()) {
                    error = true;
                    msg = "El codigo QR " + abonadoMasive.getQr() + " no existe o ya se encuentra asigando a un Abonado";
                    System.out.println(msg);
                    continue;
                }
                Optional<Asiento> asiento = iAsientoRepository.findByAsientoAndEstadoAndSublocalidad(abonadoMasive.getAsiento(), StateUtil.STATE_ACTIVE, abonadoMasive.getSubLocalidad());
                if (!asiento.isPresent()) {
                    error = true;
                    msg = "El Asiento " + abonadoMasive.getAsiento() + " no existe o ya se encuentra asigando a un Abonado";
                    System.out.println(msg);
                    continue;
                }

                Abono abono = new Abono();
                abono.setCsuscripcion(suscripcion);
                abono.setValorAbono(Double.parseDouble(abonadoMasive.getMonto()));
                abono.setCasiento(asiento.get());
                abono.setCqrcode(qrCode.get());
                abono.setCodigoAbono(abonadoMasive.getQr());
                abono.setFechaDesde(new Date());
                abono.setFechaHasta(DateUtil.getDateMax());

                //en caso de no existir la cedula del abonado
                if(abonadoMasive.getCedulaAbono()!= null && !abonadoMasive.getCedulaAbono().trim().isEmpty())
                    abono.setCedulaAbono(abonadoMasive.getCedulaAbono());
                else
                    abono.setCedulaAbono(cliente.getIdentificacion());

                //en caso de no existir el nombre del abonado
                if(abonadoMasive.getNombreAbono()!= null && !abonadoMasive.getNombreAbono().trim().isEmpty())
                    abono.setNombreAbono(abonadoMasive.getNombreAbono());
                else
                    abono.setNombreAbono(cliente.getNombres());

                abono.setForma_pago(abonadoMasive.getFormaPago());
                abono.setPromocion(abonadoMasive.getPromocion());
                abonos.add(abono);

                qrCode.get().setEstado("O");
                iqrCodeRepository.save(qrCode.get());

                asiento.get().setEstado("O");
                iAsientoRepository.save(asiento.get());
                numAbonos++;
                sumAbonos += Double.parseDouble(abonadoMasive.getMonto());

            } catch (Exception ex) {
                error = true;
                msg = "No se pudo registrar el abonado";
                log.error("Error en el registro de abonados, error: " + ex.toString());
            } finally {
                if (error) {
                    abonadoMasive.setError(true);
                    System.out.println(abonadoMasive.getMensaje());
                    abonadoMasive.setMensaje(msg);
                    iAbonadosMasiveRepository.save(abonadoMasive);
                }
            }


        }
        if (numAbonos > 0) {
            suscripcion.setValor(sumAbonos);
            suscripcion.setCantidad(numAbonos);
            iAbonoRepository.saveAll(abonos);
            iSuscripcionRepository.save(suscripcion);
        }
        return suscripcion.getCsuscripcion();
    }


}
