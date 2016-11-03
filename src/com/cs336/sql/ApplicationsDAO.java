package com.cs336.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.simple.JSONObject;

import com.cs336.obj.auction.Auction;
import com.cs336.obj.auction.Bid;
import com.cs336.obj.auction.Item;
import com.cs336.obj.ticket.Ticket;
import com.cs336.stat.SVList;
import com.cs336.usr.Admin;
import com.cs336.usr.C_Rep;
import com.cs336.usr.EndUser;

public class ApplicationsDAO {
	public Connection connection  = null;
	
	public ApplicationsDAO(){
		InitialContext ic;
		DataSource ds = null;

		try {
			ic = new InitialContext();
			ds = (DataSource) ic.lookup("java:comp/env/jdbc/cs336");
			connection = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public void closeConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int GetLogInfo(String username, String password){
		String sql = "select type_user from users where user_name=? AND user_password=?";
		int type = 0;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				type = rs.getInt("type_user");
			}
			
		}catch(SQLException se){se.printStackTrace();}
		return type;
	}
	
	public EndUser GetEndUser(String username){
		String sql = "select * from users u join end_users e on u.user_name=e.user_name where e.user_name=?";
		EndUser e = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next())
				e = new EndUser(rs);
		}catch(SQLException se){se.printStackTrace();return null;}
		return e;
	}
	
	public C_Rep GetCustRep(String username){
		String sql = "select * from users u join customer_reps c on u.user_name=c.user_name where c.user_name=?";
		C_Rep cr = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next())
				cr = new C_Rep(rs);
		}catch(SQLException se){se.printStackTrace();return null;}
		return cr;
	}
	
	public Admin GetAdmin(String username){
		String sql = "select * from users u join administrators a on u.user_name=a.user_name where a.user_name=?";
		Admin a= null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next())
				a = new Admin(rs);
		}catch(SQLException se){se.printStackTrace();return null;}
		return a;
	}
	
	public boolean SetEndUser(EndUser e){
		String sql = "insert into users (user_name, user_password, first_name, last_name, user_email, type_user) " +
					"values (?,?,?,?,?,?)";	
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, e.user_name);
			stmt.setString(2, e.user_password);
			stmt.setString(3, e.first_name);
			stmt.setString(4, e.last_name);
			stmt.setString(5, e.user_email);
			stmt.setInt(6, SVList.END_USER_TYPE);
			
			stmt.execute();

			sql = "insert into end_users(user_name, isAnonymous) values(?)";
			
			stmt = connection.prepareStatement(sql);
			stmt.setString(1,  e.user_name);
			stmt.execute();
			
		}catch(SQLException se){se.printStackTrace();return false;}
		return true;
	}
	
	public boolean SetCustRep(C_Rep c){
		String sql = "insert into users (user_name, user_password, first_name, last_name, user_email, type_user) " +
				"values (?,?,?,?,?,?)";	
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, c.user_name);
			stmt.setString(2, c.user_password);
			stmt.setString(3, c.first_name);
			stmt.setString(4, c.last_name);
			stmt.setString(5, c.user_email);
			stmt.setInt(6, SVList.CR_USER_TYPE);
			
			stmt.execute();
			
			sql = "insert into customer_reps(user_name) values(?)";
			
			stmt = connection.prepareStatement(sql);
			stmt.setString(1,  c.user_name);
			stmt.execute();
		}catch(SQLException se){se.printStackTrace();return false;}
		return true;
	}

	public boolean setTicketAnswered(Ticket t){
		String sql = "update tickets set answered=1 where ticket_id=(?)";
		
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, t.ticket_id);
			stmt.executeUpdate();
			
			sql = "insert into emails(email_from, email_to, email_subject, email_content) " +
					"values (?,?,?,?)";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, t.cr.user_name);
			stmt.setString(2, t.user.user_name);
			stmt.setString(3, t.message_header);
			stmt.setString(4, t.answer);
			
			stmt.executeUpdate();
		}catch(SQLException se){se.printStackTrace();return false;}
		return true;
	}
	/**
	 * Used to initialize an Auction_List
	 * @return null on error, or a result set of end_users, users (seller), items, active_auctions
	 */
	public ResultSet Get_Active_Auctions(){
		String sql = "select * from active_auctions a " + 
			"inner join end_users e on a.seller_id=e.end_user_id " +
			"inner join users u on e.user_name=u.user_name " +
			"inner join items i on a.item_id=i.item_id";
		ResultSet rs = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			rs = stmt.executeQuery();
		}catch(SQLException se){se.printStackTrace();return null;}
		return rs;
	}

	/**
	 * Used to get ONE full information for Auction
	 * @param auction_id
	 * @return null on error, or an Auction object
	 */
	public Auction Get_One_Auction(int auction_id){
		String sql = "select * from active_auctions a " + 
				"inner join end_users e on a.seller_id=e.end_user_id " +
				"inner join users u on e.user_name=u.user_name " +
				"inner join items i on a.item_id=i.item_id " +
				"where a.auction_id=(?)";
		Auction a = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, auction_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				a = new Auction(rs);
			}
		}catch(SQLException se){se.printStackTrace();return null;}
		return a;
	}

	/**
	 * Used to initialize BidHistory
	 * @param auction_id
	 * @return null on error, or result set of active_bids, end_users, users (bidder)
	 */
	public ResultSet Get_Bid_History(int auction_id){
		String sql = "select * from active_bids b " +
				"inner join end_users e on b.buyer_id=e.end_user_id " +
				"inner join users u on e.user_name=u.user_name " +
				"where b.auction_id=(?)";
		ResultSet rs = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, auction_id);
			rs = stmt.executeQuery();
		}catch(SQLException se){se.printStackTrace();return null;}
		return rs;
	}
	
	public ResultSet Get_One_Ticket(int ticket_id){
		String sql = "select * from tickets t " +
				"inner join end_users e on t.end_user_id=e.end_user_id " +
				"inner join users u on e.user_name=u.user_name " +
				"where t.ticket_id=(?)";
		ResultSet rs = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, ticket_id);
			rs = stmt.executeQuery();
		}catch(SQLException se){se.printStackTrace();return null;}
		return rs;
	}
	
	public ResultSet Get_Opened_Tickets(){
		String sql = "select * from tickets t " +
					"inner join end_users e on t.end_user_id=e.end_user_id " +
					"inner join users u on e.user_name=u.user_name " +
					"where t.is_open=1";
		ResultSet rs = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
		}catch(SQLException se){se.printStackTrace();return null;}
		return rs;
	}
	
	public ResultSet Get_Wish_List(EndUser u){
		String sql = "select * from wish_lists where end_user_id=(?)";
		ResultSet rs = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, u.user_id);
			rs = stmt.executeQuery();
		}catch(SQLException se){se.printStackTrace();return null;}
		return rs;
	}
	/* CREATE - INSERT INTO TABLES
	 * All Create_* methods return the id that was automatically generated
	 * returns -1 on failure
	 */
	public int Create_Item(Item i){
		String sql = "insert into items(item_name, item_plat_version, item_company, " +
				"item_RAM, item_storage, camera_q, " +
				"dim_x, dim_y, dim_display, dim_thickness, weight, battery_time, " +
				"description, image) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) "; // 14
		int id = -1;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, i.item_name);
			stmt.setFloat(2, i.item_plat_version);
			stmt.setString(3, i.item_company);
			int currentPos = 4;
			for(int x=currentPos; x<currentPos+SVList.item_att_int.length; x++){
				stmt.setInt(x, i.item_att_int[x-currentPos]);
			}
			currentPos += SVList.item_att_int.length;
			for(int x=currentPos; x<currentPos+SVList.item_att_float.length; x++){
				stmt.setFloat(x, i.item_att_float[x-currentPos]);
			}
			currentPos += SVList.item_att_float.length;
			for(int x=currentPos; x<currentPos+SVList.item_att_str.length; x++){
				stmt.setString(x, i.item_att_str[x-currentPos]);
			}
			if(stmt.executeUpdate() == 0)
				return -1;
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next())
				id = rs.getInt(1);

		}catch(SQLException se){se.printStackTrace();return -1;}
		return id;
	}

	public int Create_Auction(Auction a){
		String sql = "insert into active_auctions(seller_id, item_id, end_date, public_increment, secret_min_price) " +
				"values(?, ?, ?, ?, ?)";
		int id = -1;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, a.seller.user_id);
			stmt.setInt(2, a.item.item_id);
			stmt.setTimestamp(3, (Timestamp) a.end_date);
			stmt.setInt(4, a.public_increment);
			stmt.setInt(5, a.secret_min_price);
			
			if(stmt.executeUpdate() == 0)
				return -1;
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next())
				id = rs.getInt(1);
		}catch(SQLException se){se.printStackTrace();return -1;}
		return id;
	}
	
	/* create_bid is tricky, because we have a trigger doing the insert for us.
	 * In order to get the bid_id, we need to manually search for it.
	 */
	public int Create_Bid(Bid b, int auction_id){
		String sql = "insert into active_bid_input(auction_id, buyer_id, bid_amount, bid_upper_limit) " +
				"values(?, ?, ?, ?)";
		int id = -1;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, auction_id);
			stmt.setInt(2, b.buyer.user_id);
			stmt.setInt(3, b.bid_amount);
			stmt.setInt(4, b.bid_upper_limit);
			
			if(stmt.executeUpdate() == 0)
				return -1;
			
			sql = "select bid_id from active_bids " +
					"where auction_id=(?) AND buyer_id=(?) AND bid_amount=(?) AND bid_upper_limit=(?)";
			
			stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, auction_id);
			stmt.setInt(2, b.buyer.user_id);
			stmt.setInt(3, b.bid_amount);
			stmt.setInt(4, b.bid_upper_limit);
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				id = rs.getInt(1);
		}catch(SQLException se){se.printStackTrace();return -1;}
		return id;
	}

	public int Create_Ticket(Ticket t){
		String sql = "insert into tickets(end_user_id, message_header, message_body, is_open, answered) " +
				"values(?, ?, ?, ?, ?)";
		int id = -1;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, t.user.user_id);
			stmt.setString(2, t.message_header);
			stmt.setString(3, t.message_body);
			stmt.setBoolean(4, true);
			stmt.setBoolean(5, false);
			
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next())
				id = rs.getInt(1);
		}catch(SQLException se){se.printStackTrace();return -1;}
		return id;
	}
	
	public int Create_Wish_List_Item(EndUser u, Item i){
		String sql = "insert into wish_lists(end_user_id, phone_plat_version, phone_company) values(?, ?, ?)";
		int id = -1;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, u.user_id);
			stmt.setFloat(2, i.item_plat_version);
			stmt.setString(3, i.item_company);
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next())
				id = rs.getInt(1);
		}catch(SQLException se){se.printStackTrace();return -1;}
		return id;
	}
	
	public boolean Update_Auction(Auction a){
		String sql = "update active_auctions " + 
					"set item_id=(?)" + ", " +
					"set end_date=(?)" + ", " + 
					"set public_increment=(?)" + ", " +
					"set secret_min_price=(?) " +
					"where auction_id=(?)";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, a.item.item_id);
			stmt.setTimestamp(2, (Timestamp)a.end_date);
			stmt.setInt(3, a.public_increment);
			stmt.setInt(4, a.secret_min_price);
			stmt.setInt(5, a.auction_id);
			
			stmt.executeUpdate();
		}catch(SQLException se){se.printStackTrace();return false;}
		return true;
	}
	
	public boolean Update_Item(Item i){
		String sql = "update items " +
				"set item_name=(?)" + ", " +
				"set item_plat_version=(?)" + ", " +
				"set item_company=(?)" + ", " + 
				"set item_RAM=(?)" + ", " +
				"set item_storage=(?)" + ", " +
				"set camera_q=(?)" + ", " +
				"set dim_x=(?)" + ", " +
				"set dim_y=(?)" + ", " +
				"set dim_display=(?)" + ", " +
				"set dim_thickness=(?)" + ", " +
				"set weight=(?)" + ", " +
				"set battery_time=(?)" + ", " +
				"set description=(?)" + ", " +
				"set image=(?) " +
				"where item_id=(?)";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, i.item_name);
			stmt.setFloat(2, i.item_plat_version);
			stmt.setString(3, i.item_company);
			int currentPos = 4;
			for(int x=currentPos; x<currentPos+SVList.item_att_int.length; x++){
				stmt.setInt(x, i.item_att_int[x-currentPos]);
			}
			currentPos += SVList.item_att_int.length;
			for(int x=currentPos; x<currentPos+SVList.item_att_float.length; x++){
				stmt.setFloat(x, i.item_att_float[x-currentPos]);
			}
			currentPos += SVList.item_att_float.length;
			for(int x=currentPos; x<currentPos+SVList.item_att_str.length; x++){
				stmt.setString(x, i.item_att_str[x-currentPos]);
			}
			stmt.setInt(15, i.item_id);
			
			stmt.executeUpdate();
		}catch(SQLException se){se.printStackTrace();return false;}
		return true;
	}
	
	/* deletes auction AND the item */
	public boolean Delete_Auction(Auction a){
		String sql = "delete from active_auctions where auction_id=(?)";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, a.auction_id);
			stmt.execute();
			
			sql = "delete from items where item_id=(?)";
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, a.item.item_id);
			stmt.execute();
		}catch(SQLException se){se.printStackTrace();return false;}
		return true;
	}
	
	public boolean Delete_Wish_List(EndUser u, Item i){
		String sql = "delete from wish_lists where end_user_id=(?) AND phone_plat_version=(?) AND phone_company=(?)";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, u.user_id);
			stmt.setFloat(2, i.item_plat_version);
			stmt.setString(3, i.item_company);
			stmt.execute();
			
		}catch(SQLException se){se.printStackTrace();return false;}
		return true;
	}
	
	public ResultSet Prepare_Summary_Report(){
		String sql = "select * from auction_history ah " +
				"inner join items i on ah.item_id=i.item_id " +
				"inner join end_users s on ah.seller_id=s.end_user_id " +
				"inner join users seller on seller.user_name=s.user_name " +
				"inner join bid_history bh on bh.bid_id=ah.winning_bid_id " +
				"inner join end_users b on bh.buyer_id=b.end_user_id " +
				"inner join users buyer on buyer.user_name=b.user_name " +
				"where ah.winning_bid_id is not null";
		ResultSet rs = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
		}catch(SQLException se){se.printStackTrace();return null;}
		return rs;
	}
	
	public ResultSet Get_Best_Selling_EndUser(){
		String sql = "select s.user_name, SUM(bh.bid_amount) from auction_history ah " +
				"inner join end_users s on ah.seller_id=s.end_user_id " +
				"inner join bid_history bh on ah.winning_bid_id=bh.bid_id " +
				"where ah.winning_bid_id is not null " +
				"group by end_user_id " +
				"order by SUM(bh.bid_amount) DESC " +
				"LIMIT 1";
		ResultSet rs = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
		}catch(SQLException se){se.printStackTrace();return null;}
		return rs;
	}
	
	public ResultSet Get_Best_Selling_Item(){
		String sql = "select i.item_plat_version, i.item_company, SUM(bh.bid_amount) from auction_history ah " +
				"inner join items i on ah.item_id=i.item_id " +
				"inner join bid_history bh on ah.winning_bid_id=bh.bid_id " +
				"where ah.winning_bid_id is not null " +
				"group by i.item_plat_version, i.item_company " +
				"order by SUM(bh.bid_amount) DESC " +
				"LIMIT 1";
		ResultSet rs = null;
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
		}catch(SQLException se){se.printStackTrace();return null;}
		return rs;
	}
	
	public JSONObject Get_User_Report(String user){
		String sql = "select SUM(bh.bid_amount) from auction_history ah " +
				"inner join end_users s on ah.seller_id=s.end_user_id " +
				"inner join bid_history bh on ah.winning_bid_id=bh.bid_id " +
				"where ah.winning_bid_id is not null AND s.user_name=(?)";
		JSONObject value = new JSONObject();
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, user);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				value.put("mark", user);
				value.put("total", rs.getInt("SUM(bh.bid_amount)"));
			}
		}catch(SQLException se){se.printStackTrace();return null;}
		return value;
	}
	
	public JSONObject Get_OS_Report(String company){
		String sql = "select SUM(bh.bid_amount) from auction_history ah " +
				"inner join items i on ah.item_id=i.item_id " +
				"inner join bid_history bh on ah.winning_bid_id=bh.bid_id " +
				"where ah.winning_bid_id is not null AND i.item_company=(?)";
		JSONObject value = new JSONObject();
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, company);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				value.put("mark", company);
				value.put("total", rs.getInt("SUM(bh.bid_amount)"));
			}
		}catch(SQLException se){se.printStackTrace();return null;}
		return value;
	}
}
