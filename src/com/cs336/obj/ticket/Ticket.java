package com.cs336.obj.ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cs336.usr.C_Rep;
import com.cs336.usr.EndUser;

public class Ticket implements Comparable<Ticket>{
	public int ticket_id;
	public String message_header;
	public String message_body;
	public Date ticket_date;
	public boolean is_open;
	public boolean answered;
	
	public String answer;
	
	public EndUser user;
	public C_Rep cr;
	
	public Ticket(ResultSet rs){
		try {
			this.ticket_id = rs.getInt("ticket_id");
			this.message_header = rs.getString("message_header");
			this.message_body = rs.getString("message_body");
			this.ticket_date = rs.getTimestamp("ticket_date");
			this.is_open = rs.getBoolean("is_open");
			this.answered = rs.getBoolean("answered");
			
			this.user = new EndUser(rs);
		} catch (SQLException e) {e.printStackTrace();}
	}
	public Ticket(EndUser u, HttpServletRequest request){
		this.user = u;
		this.message_header = (String)request.getParameter("title");
		this.message_body = (String)request.getParameter("description");
	}
	
	public int getTicket_id(){return ticket_id;}
	public String getMessage_header(){return message_header;}
	public String getMessage_body(){return message_body;}
	public Date getTicket_date(){return ticket_date;}
	public boolean getIs_open(){return is_open;}
	public boolean getAnswered(){return answered;}
	public EndUser getUser(){return user;}
	public C_Rep getCr(){return cr;}
	@Override
	public int compareTo(Ticket t) {
		return this.ticket_date.compareTo(t.ticket_date);
	}
}
