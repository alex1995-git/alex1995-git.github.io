package com.depcue.restupload;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.depcue.model.Abono;
import com.depcue.repository.IAbonoRepository;
import com.depcue.scheduletask.ScheduleTask;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Controller
public class RestUploadController {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduleTask.class);

	@Autowired
	private IAbonoRepository repAbono;

	@GetMapping("/upload")
	public String index() {
		return "upload-index";
	}

	@PostMapping("/upload-csv-file")
	public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

		// validate file
		if (file.isEmpty()) {
			model.addAttribute("message", "Please select a CSV file to upload.");
			model.addAttribute("status", false);
		} else {

			// parse CSV file to create a list of `User` objects
			try (Reader reader = new BufferedReader(
					new InputStreamReader(file.getInputStream()))) {
				
				CsvToBean<ModelUpload> csvToBean = new CsvToBeanBuilder<ModelUpload>(reader)
						.withType(ModelUpload.class)
						.withIgnoreLeadingWhiteSpace(true)
						.build();

				List<ModelUpload> listAbonos = csvToBean.parse();
				List<ModelUpload> listAbonosCargados = new LinkedList<>();
				int contRegs = 0;
				LOG.info("Begin Charging DataBase CSV");
				
				
				for (ModelUpload u : listAbonos) {
					
					Optional<Abono> a = repAbono.findByEstadoAndCodigoAbono("A", u.getUrl());
					// if (listAbono.isEmpty() || listAbono == null) {
					Abono abono = new Abono();
					if (a.isPresent()) {
						abono = a.get();
					} else {
						listAbonosCargados.add(u);
					}
					abono.setCodigoAbono(u.getCodigo());
//					abono.setNombres(u.getNombres().toUpperCase());
//					abono.setCedula(u.getCedula());
//					abono.setCorreo(u.getEmail());
//					abono.setTelefono(u.getTelefono());
//					abono.setDireccion(u.getDireccion());
//					abono.setLocalidad(u.getLocalidad().toUpperCase());
//					abono.setAsiento(u.getAsiento().toUpperCase());
//					abono.setTipo(u.getTipo().toUpperCase());

					repAbono.saveAndFlush(abono);
					contRegs++;
				}
				LOG.info("Count Registers: " + contRegs + " abonos nuevos detectados");
				LOG.info("End Charging DataBase CSV");
				// save users list on model
				reader.close();
				model.addAttribute("users", listAbonosCargados);
				model.addAttribute("status", true);

			} catch (Exception ex) {
				model.addAttribute("message", "An error occurred while processing the CSV file.");
				model.addAttribute("status", false);
			}
		}

		return "file-upload-status";

	}

	String decodeText(String input, String encoding) throws IOException {
		return new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(input.getBytes()), Charset.forName(encoding)))
						.readLine();
	}
}
