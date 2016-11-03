package com.cs336.obj.auction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* Auctions and Bid_History are loaded with 2 different queries */
public class Bid_History {
	public ArrayList<Bid> bids;
	public Auction auction;
	
	public Bid winningBid;
	
	/* This Result Set is for making summary reports */
	public Bid_History(Auction a, ResultSet rs, int summary_report){
		winningBid = new Bid(rs, summary_report);
	}
	/* This Result Set contains active_bids / bid_history, end_user*/
	public Bid_History(Auction a, ResultSet rs) throws SQLException{
		bids = new ArrayList<Bid>();
		auction = a;
		while(rs.next())
			bids.add(new Bid(rs));
		
		if(bids.size() > 0){
			Collections.sort(bids);
			//winning bid should now be the highest bid available after sorting largest->smallest
			winningBid = bids.get(0);
		}
	}
	
	public boolean isValidBid(Bid b){
		if(bids.size() == 0 && winningBid == null)
			return true;
		return b.bid_amount >= winningBid.bid_amount + auction.public_increment;
	}
	
	public ArrayList<Bid> getBids(){return bids;}
	public Auction getAuction(){return auction;}
	public Bid getWinningBid(){return winningBid;}
}
