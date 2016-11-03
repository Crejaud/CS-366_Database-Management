package com.cs336.usr;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.cs336.obj.auction.Wish_List;

public class EndUser extends User{
	public boolean isAnonymous;
	public Wish_List wl;
	
	public EndUser(ResultSet rs) throws SQLException{
		super(rs);
		this.user_type = rs.getInt("type_user");
		this.user_id = rs.getInt("end_user_id");
		this.isAnonymous = rs.getBoolean("isAnonymous");
	}
	public EndUser(HttpServletRequest request){
		super(request);
		this.user_type = 1;
	}
	public EndUser(ResultSet rs, String ref) throws SQLException{
		super(rs, ref);
		this.user_type = rs.getInt(ref + ".type_user");
		this.user_id = rs.getInt(ref.charAt(0) + ".end_user_id");
		this.isAnonymous = rs.getBoolean(ref.charAt(0) + ".isAnonymous");
	}
	
	public void grab_Wish_List(ResultSet rs){
		wl = new Wish_List(rs);
	}
	
	public Wish_List getWl(){return wl;}
}
