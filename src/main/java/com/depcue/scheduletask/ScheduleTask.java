package com.depcue.scheduletask;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


import com.depcue.model.RegistroAbono;
import com.depcue.repository.IRegistroAbonoRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleTask extends QuartzJobBean{

	
	@Value("${time.minutes.unlock}")
	private int minUnlock;
	
	@Autowired
	private IRegistroAbonoRepository repoda;
	
	 private static final Logger LOG = LoggerFactory.getLogger(ScheduleTask.class);

	 private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	 private static final String DATE_FORMATTER= "yyyy-MM-dd HH:mm:ss";
	 
	    @Override
	    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	    	List<RegistroAbono> listaRegAbonos = repoda.findByEstado("A");
	        LOG.info("The current time is: " + dateFormat.format(new Date())+" Regs:"+listaRegAbonos.size()+" Mins:"+minUnlock);
	       
	       for (RegistroAbono reg : listaRegAbonos) {
	    	   LocalDateTime fechaIngreso= LocalDateTime.parse(reg.getFechaHoraEntrada(),DateTimeFormatter.ofPattern(DATE_FORMATTER));
	    	   LocalDateTime fechaDesbloqueo= addMinutesToLocalDateTime(minUnlock, fechaIngreso);
	    	   LocalDateTime fechaActual= LocalDateTime.now();
	    	   if (fechaActual.isAfter(fechaDesbloqueo)) {
	    		   LOG.info("fecha ingreso:" + fechaIngreso.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
	    		   +" - fecha debloqueo:"+ fechaDesbloqueo.format(DateTimeFormatter.ofPattern(DATE_FORMATTER)));
	    		   reg.setEstado("D");
	    		   reg.setFechaHoraSalida(fechaDesbloqueo.format(DateTimeFormatter.ofPattern(DATE_FORMATTER)));
	    		   repoda.saveAndFlush(reg);
	    	   }
	    	}
	    	
	    }
	    
	    private static final long ONE_MINUTE_IN_MILLIS = 60000;

		private static Date addMinutesToDate(int minutes, Date beforeTime) {
			try {
				long curTimeInMs = beforeTime.getTime();
				Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
				return afterAddingMins;
			} catch (Exception e) {
				e.printStackTrace();
				return new Date();
			}
		}

		private static LocalDateTime addMinutesToLocalDateTime(int minutes, LocalDateTime beforeTime) {
			try {
				return beforeTime.plusMinutes(minutes);
			} catch (Exception e) {
				e.printStackTrace();
				return LocalDateTime.now();
			}
		}
}
