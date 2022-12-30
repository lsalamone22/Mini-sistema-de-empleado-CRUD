package com.gestion.empleados;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassowrdGenerator {
	
	public static void main(String[] args) {
		
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		String rawPassword ="12345";
		String encodedPassword  = encoder.encode(rawPassword);//va a codificar esta password
		
		System.out.print(encodedPassword);
		
	}

}
