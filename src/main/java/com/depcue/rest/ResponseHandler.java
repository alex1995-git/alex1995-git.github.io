package com.depcue.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
            map.put("message", message);
            map.put("status", status.value());
            map.put("data", responseObj);

            return new ResponseEntity<Object>(map,status);
    }
	

	public static Date parseStringToDate(String date) {
		try {
			return (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).parse(date) ;
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}  
	}
	
	public static String parseDateToString(Date date) {
		try {
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		} 
	}
	
	public static String recuperaBodyContenido(String html) {
		String inicio="<p class=\"item center\">";
		String fin="</p>";
		String retorno="";
		
		try {
			retorno = html.substring(
					html.lastIndexOf(inicio)+inicio.length(), 
					html.lastIndexOf(fin)
					);			
		}catch(Exception e) {
			e.printStackTrace();
			retorno="";
		}
		
		 return retorno;
	}
}
