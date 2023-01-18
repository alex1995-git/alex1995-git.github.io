package com.depcue.util;

import com.depcue.model.AbonadosMasive;
import com.depcue.model.util.ResponseGeneric;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Slf4j
public class UtilCargaMasiva {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    /*
     * private String[] HEADERs = { "SECUENCIA", "LOCALIDAD", "SUBLOCALIDAD",
     * "COD LETRAS", "CODIGO COMPLETO", "QR",
     * "TARIFA", "TIPO TARIFA", "TIPO", "ASIENTO", "APELLIDOS", "NOMBRES", "CEDULA",
     * "CORREO", "TELEFONO",
     * "OBSERVACION" };
     */

    private String[] HEADERs = {"SECUENCIA",
            "LOCALIDAD",
            "SUBLOCALIDAD",
            "COD LETRAS",
            "CODIGO COMPLETO",
            "QR",
            "TARIFA",
            "TIPO TARIFA",
            "TIPO",
            "ASIENTO",
            "CEDULA ABONO",
            "NOMBRE ABONO",
            "FECHA",
            "APELLIDOS",
            "NOMBRES",
            "CEDULA",
            "CORREO",
            "TELEFONO",
            "FORMA DE PAGO",
            "MONTO",
            "PROMOCION",
            "OBSERVACION"};
    static String SHEET = "BASE DE DATOS";
    // static int first = 3;

    public ResponseGeneric execute(InputStream is) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            UUID uuid = UUID.randomUUID();
            Date fechaRegistro = new Date();
            String username = authentication.getName();
            //  Workbook workbook = new XSSFWorkbook(is);
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<AbonadosMasive> abonados = new ArrayList<AbonadosMasive>();
            // int rowNumber = 0;
            Map<Integer, String> map = new HashMap<>();
            boolean cabeceraEncontrada = false;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (!cabeceraEncontrada) {
                    Iterator<Cell> cellsInRowH = currentRow.iterator();

                    for (int i = 0; i < HEADERs.length; i++) {
                        if (cellsInRowH.hasNext()) {
                            String value = getValue((XSSFCell) cellsInRowH.next()).trim();
                            // System.out.println(HEADERs[i] + " - " + value);
                            /*
                             * si campo cabecera comenzo siendo igual pero siguientes campos son diferentes
                             * presenta error
                             */
                            if (i != 0 && !HEADERs[i].equalsIgnoreCase(value)) {
                                return new ResponseGeneric(true, "msg_error_cabecera_base_datos_xlsx");
                            }
                            /* si campo cabecera no es igual ,sigue a la proxima fila */
                            if (!HEADERs[i].equalsIgnoreCase(value)) {
                                i = HEADERs.length;
                                map = new HashMap<>();
                                continue;
                            }
                            /* agregamos campos a mapa */
                            if (HEADERs[i].equalsIgnoreCase(value)) {
                                map.put(i + 1, value);
                            }
                            /*
                             * si al terminar de recorrer la cabecera todos son iguales setea flag de
                             * cabeceraencontrada en true
                             */
                            if (i == HEADERs.length - 1 && HEADERs[i].equalsIgnoreCase(value)) {
                                cabeceraEncontrada = true;
                                continue;
                            }
                        }
                    }
                    // if(!cabeceraEncontrada){
                    continue;
                    // }
                }

                // boddy
                Iterator<Cell> cellsInRow = currentRow.iterator();
                AbonadosMasive abonadosMasive = new AbonadosMasive();
                int cellIdx = 1;
                boolean agregar = cellsInRow.hasNext();
                while (cellsInRow.hasNext()) {
                    XSSFCell cell = (XSSFCell) cellsInRow.next();

                    String nombreCol = map.get(cellIdx).toUpperCase().trim();
                    String valueCell = getValue(cell).replaceAll("\n", "");

                    if (nombreCol.equals(HEADERs[0].toUpperCase().trim())) {
                        abonadosMasive.setSecuencial(valueCell);
                        if (abonadosMasive.getSecuencial().trim().isEmpty()) {
                            agregar = false;
                            break;
                        }
                    } else if (nombreCol.equals(HEADERs[1].toUpperCase().trim())) {
                        abonadosMasive.setLocalidad(valueCell);
                        if (abonadosMasive.getLocalidad().trim().isEmpty()) {
                            agregar = false;
                            break;
                        }
                    } else if (nombreCol.equals(HEADERs[2].toUpperCase().trim())) {
                        abonadosMasive.setSubLocalidad(valueCell);
                        if (abonadosMasive.getSubLocalidad().trim().isEmpty()) {
                            agregar = false;
                            break;
                        }
                    } else if (nombreCol.equals(HEADERs[3].toUpperCase().trim())) {
                        abonadosMasive.setAbreLocalidad(valueCell);
                        if (abonadosMasive.getAbreLocalidad().trim().isEmpty()) {
                            agregar = false;
                            break;
                        }
                    } else if (nombreCol.equals(HEADERs[4].toUpperCase().trim())) {
                        abonadosMasive.setCodigoLocalidad(valueCell);
                        if (abonadosMasive.getCodigoLocalidad().trim().isEmpty()) {
                            agregar = false;
                            break;
                        }
                    } else if (nombreCol.equals(HEADERs[5].toUpperCase().trim())) {
                        abonadosMasive.setQr(valueCell);
                        if (abonadosMasive.getQr().trim().isEmpty()) {
                            agregar = false;
                            break;
                        }
                    } else if (nombreCol.equals(HEADERs[6].toUpperCase().trim())) {
                        abonadosMasive.setTarifa(valueCell);
                    } else if (nombreCol.equals(HEADERs[7].toUpperCase().trim())) {
                        abonadosMasive.setTipoTarifa(valueCell);
                    } else if (nombreCol.equals(HEADERs[8].toUpperCase().trim())) {
                        abonadosMasive.setTipoCliente(valueCell);
                    } else if (nombreCol.equals(HEADERs[9].toUpperCase().trim())) {
                        abonadosMasive.setAsiento(valueCell);
                    } else if (nombreCol.equals(HEADERs[10].toUpperCase().trim())) {
                        abonadosMasive.setCedulaAbono(valueCell);
                    } else if (nombreCol.equals(HEADERs[11].toUpperCase().trim())) {
                        abonadosMasive.setNombreAbono(valueCell);
                    } else if (nombreCol.equals(HEADERs[12].toUpperCase().trim())) {
                        try {
                            abonadosMasive.setFechaSubscripcion(cell.getDateCellValue().toString());
                        } catch (Exception e) {
                            abonadosMasive.setFechaSubscripcion(cell.getRawValue());
                        }
                    } else if (nombreCol.equals(HEADERs[13].toUpperCase().trim())) {
                        abonadosMasive.setApellidos(valueCell);
                    } else if (nombreCol.equals(HEADERs[14].toUpperCase().trim())) {
                        abonadosMasive.setNombres(valueCell);
                    } else if (nombreCol.equals(HEADERs[15].toUpperCase().trim())) {
                        abonadosMasive.setCedula(valueCell);
                    } else if (nombreCol.equals(HEADERs[16].toUpperCase().trim())) {
                        abonadosMasive.setCorreo(valueCell);
                    } else if (nombreCol.equals(HEADERs[17].toUpperCase().trim())) {
                        abonadosMasive.setTelefono(valueCell);
                    } else if (nombreCol.equals(HEADERs[18].toUpperCase().trim())) {
                        abonadosMasive.setFormaPago(valueCell);
                    } else if (nombreCol.equals(HEADERs[19].toUpperCase().trim())) {
                        abonadosMasive.setMonto(valueCell);
                    } else if (nombreCol.equals(HEADERs[20].toUpperCase().trim())) {
                        abonadosMasive.setPromocion(valueCell);
                    } else if (nombreCol.equals(HEADERs[15].toUpperCase().trim())) {
                        abonadosMasive.setObservacion(valueCell);
                    }
                    abonadosMasive.setFechaRegistro(fechaRegistro);
                    abonadosMasive.setUsernameCarga(username);
                    abonadosMasive.setCodeUnico(uuid.toString());
                    cellIdx++;
                }
                if (agregar) {
                    abonados.add(abonadosMasive);
                }
                // rowNumber++;
            }

            return new ResponseGeneric(false, uuid.toString(), abonados);
        } catch (Exception ex) {
            log.error("Error occurred", ex);
            return new ResponseGeneric(false, "msg_error_al_leer_xlsx");
        }

    }

    private String getValue(XSSFCell value) {
        if (value == null) {
            return "";
        }
        try {
            return value.getStringCellValue();
        } catch (Exception e) {
            try {
                return value.getRawValue();
            } catch (Exception ee) {
                return "";
            }
        }
    }

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
}
