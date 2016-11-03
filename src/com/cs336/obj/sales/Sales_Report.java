package com.cs336.obj.sales;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cs336.obj.auction.Auction;
import com.cs336.obj.auction.Bid_History;
import com.cs336.obj.auction.Item;

public class Sales_Report {
	int total_earnings;
	
	String bestSellingUserID;
	int userID_earnings;
	
	Item bestSellingItem;
	int item_earnings;
	
	ArrayList<Auction> won_auctions;
	
	public Sales_Report(ResultSet rs){
		total_earnings = 0;
		won_auctions = new ArrayList<Auction>();
		try {
			while(rs.next()){
				Auction a = new Auction(rs);
				won_auctions.add(a);
				a.set_Bid_History(new Bid_History(a, rs, 1));
				total_earnings += a.bid_history.winningBid.bid_amount;
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	/* these resultset is result of a group by query */
	public void setBestSellingEndUser(ResultSet rs){
		try {
			if(rs.next()){
				bestSellingUserID = rs.getString("user_name");
				userID_earnings = rs.getInt("SUM(bh.bid_amount)");
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	public void setBestSellingItem(ResultSet rs){
		try {
			if(rs.next()){
				bestSellingItem = new Item(rs, 1, 1);
				item_earnings = rs.getInt("SUM(bh.bid_amount)");
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public int getTotal_earnings(){return total_earnings;}
	public String getBestSellingUserID(){return bestSellingUserID;}
	public int getUserID_earnings(){return userID_earnings;}
	public Item getBestSellingItem(){return bestSellingItem;}
	public int getItem_earnings(){return item_earnings;}
}
