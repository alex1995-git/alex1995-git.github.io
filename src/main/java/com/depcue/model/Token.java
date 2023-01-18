package com.depcue.model;

import java.io.Serializable;

public class Token  implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;
	
	private final String jwttoken;

	public Token(String jwttoken) {
		this.jwttoken = jwttoken;
	}


	public String getToken() {
		return this.jwttoken;
	}
}
