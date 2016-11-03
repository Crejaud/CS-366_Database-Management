package com.cs336.usr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

public class User {
	public String user_name;
	public String user_password;
	public int user_type;
	public int user_id;
	public String first_name;
	public String last_name;
	public String user_email;
	
	public ArrayList<Alert> alerts;
	
	public User(ResultSet rs) throws SQLException{
		alerts = new ArrayList<Alert>();
		this.user_name = rs.getString("user_name");
		this.user_password = rs.getString("user_password");
		this.first_name = rs.getString("first_name");
		this.last_name = rs.getString("last_name");
		this.user_email = rs.getString("user_email");
	}
	/* Used for creation of an account */
	public User(HttpServletRequest request){
		alerts = new ArrayList<Alert>();
		this.user_name = (String)request.getParameter("user");
		this.user_password = (String)request.getParameter("pass");
		this.first_name = (String)request.getParameter("first");
		this.last_name = (String)request.getParameter("last");
		this.user_email = (String)request.getParameter("email");
	}
	public User(ResultSet rs, String ref) throws SQLException{
		alerts = new ArrayList<Alert>();
		this.user_name = rs.getString(ref + ".user_name");
		this.user_password = rs.getString(ref + ".user_password");
		this.first_name = rs.getString(ref + ".first_name");
		this.last_name = rs.getString(ref + ".last_name");
		this.user_email = rs.getString(ref + ".user_email");
	}
	
	public String toString(){
		return user_name + ": " + first_name + " " + last_name + " " + user_email + " ";
	}
	
	public String getUser_name(){return user_name;}
	public int getUser_id(){return user_id;}
	public String getFirst_name(){return first_name;}
	public String getLast_name(){return last_name;}
	public String getUser_email(){return user_email;}
}
