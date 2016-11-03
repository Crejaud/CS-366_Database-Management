package com.cs336.obj.auction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cs336.usr.EndUser;

public class Bid implements Comparable<Bid>{
	public int bid_id;
	public Date bid_date;
	public int bid_amount;
	public int bid_upper_limit;

	public EndUser buyer;
	
	public Bid(ResultSet rs){
		try {
			bid_id = rs.getInt("bid_id");
			bid_date = rs.getTimestamp("bid_date");
			bid_amount = rs.getInt("bid_amount");
			bid_upper_limit = rs.getInt("bid_upper_limit");
			buyer = new EndUser(rs);
		} catch (SQLException e) {e.printStackTrace();}
	}
	public Bid(EndUser u, HttpServletRequest request){
		this.buyer = u;
		this.bid_amount = Math.round(Float.parseFloat(request.getParameter("bid")));
		this.bid_upper_limit = Math.round(Float.parseFloat(request.getParameter("upper_limit")));
	}
	public Bid(ResultSet rs, int summary_report){
		try {
			bid_id = rs.getInt("bid_id");
			bid_date = rs.getTimestamp("bid_date");
			bid_amount = rs.getInt("bid_amount");
			bid_upper_limit = rs.getInt("bid_upper_limit");
			this.buyer = new EndUser(rs, "buyer");
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public int getBid_id(){return bid_id;}
	public Date getBid_date(){return bid_date;}
	public int getBid_amount(){return bid_amount;}
	public int getBid_upper_limit(){return bid_upper_limit;}
	public EndUser getBuyer(){return buyer;}
	@Override
	public int compareTo(Bid b) {
		return b.bid_amount - this.bid_amount;
	}
}
