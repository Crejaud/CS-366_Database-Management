package com.cs336.obj.ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.cs336.obj.auction.Auction;

public class Ticket_List {
	public ArrayList<Ticket> ticket_list;
	
	public Ticket_List(ResultSet rs) throws SQLException{
		ticket_list = new ArrayList<Ticket>();
		
		// add to auction_list
		while(rs.next())
			ticket_list.add(new Ticket(rs));
		
		// sort the auction list from ending_soon -> ending later
		Collections.sort(ticket_list);
	}
	
	public ArrayList<Ticket> getTicket_list(){return ticket_list;}
}
