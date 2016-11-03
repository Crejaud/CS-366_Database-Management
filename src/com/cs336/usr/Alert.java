package com.cs336.usr;

import java.util.Date;

public class Alert {
	String date_created, message;
	
	public Alert(String m){
		message = m;
		date_created = new Date().toString();
	}
}
