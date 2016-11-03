package com.cs336.obj.auction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Auction_List {
	public ArrayList<Auction> auction_list;

	/* This RS contains info for auction, end_user, and items */
	public Auction_List(ResultSet rs) throws SQLException{
		auction_list = new ArrayList<Auction>();
		
		// add to auction_list
		while(rs.next())
			auction_list.add(new Auction(rs));
		
		// sort the auction list from ending_soon -> ending later
		Collections.sort(auction_list);
	}
	
	public void printInfo(){
		System.out.println(toString());
	}
	public String toString(){
		String value = "";
		for(Auction a: auction_list){
			value += a.auction_id + " ";
		}
		return value;
	}
	
	public ArrayList<Auction> getAuction_list(){
		return auction_list;
	}
}
