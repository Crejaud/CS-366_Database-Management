package com.cs336.obj.auction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cs336.usr.EndUser;

public class Auction implements Comparable<Auction>{
	public int auction_id;
	public EndUser seller;
	public Item item;
	public boolean is_wish_list = false;
	/* contains list of bids and a winning bid (currently) */
	public Bid_History bid_history;
	
	public Date start_date, end_date;
	
	public int public_increment, secret_min_price;
	
	/* This RS contains info for auction, end_user, and items */
	public Auction(ResultSet rs){
		try {
			auction_id = rs.getInt("auction_id");
			start_date = rs.getTimestamp("start_date");
			end_date = rs.getTimestamp("end_date");
			System.out.println(end_date);
			public_increment = rs.getInt("public_increment");
			secret_min_price = rs.getInt("secret_min_price");
			
			//set seller
			seller = new EndUser(rs);
			//set item
			item = new Item(rs);
		} catch (SQLException e) {e.printStackTrace();}
	}
	public Auction(EndUser user, Item i, HttpServletRequest request){
		this.seller = user;
		this.item = i;
		String e_date = (String)request.getParameter("end_date");
		this.end_date = Timestamp.valueOf(e_date.replace("T"," "));
		this.public_increment = Math.round(Float.parseFloat(request.getParameter("bid_increment")));
		this.secret_min_price = Math.round(Float.parseFloat(request.getParameter("min_secret")));
	}
	public Auction(ResultSet rs, int summary_report){
		try {
			auction_id = rs.getInt("auction_id");
			start_date = rs.getTimestamp("start_date");
			end_date = rs.getTimestamp("end_date");
			public_increment = rs.getInt("public_increment");
			secret_min_price = rs.getInt("secret_min_price");
			
			//set seller
			seller = new EndUser(rs, "seller");
			//set item
			item = new Item(rs);
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	public void changeAuction(HttpServletRequest request){
		item.changeItem(request);
		String e_date = (String)request.getParameter("end_date");
		this.end_date = Timestamp.valueOf(e_date.replace("T"," "));
		this.public_increment = Math.round(Float.parseFloat(request.getParameter("bid_increment")));
		this.secret_min_price = Math.round(Float.parseFloat(request.getParameter("min_secret")));
	}
	
	//set bid_history
	public void set_Bid_History(Bid_History bh){bid_history = bh;}
	//see if this item is in wish_list of a user
	public void set_Wish_List(Wish_List wl){
		for(Item i: wl.wish_list){
			if(item.equals(i))
				is_wish_list = true;
		}
	}
	//getters
	public int getAuction_id(){return auction_id;}
	public Item getItem(){return item;}
	public EndUser getSeller(){return seller;}
	public Bid_History getBid_history(){return bid_history;}
	public Date getStart_date(){return start_date;}
	public Date getEnd_date(){return end_date;}
	public int getPublic_increment(){return public_increment;}
	public int getSecret_min_price(){return secret_min_price;}
	public boolean getIs_wish_list(){return is_wish_list;}
	@Override
	public int compareTo(Auction a) {
		return this.end_date.compareTo(a.end_date);
	}
}
