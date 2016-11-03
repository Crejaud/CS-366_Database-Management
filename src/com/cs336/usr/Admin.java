package com.cs336.usr;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.cs336.stat.SVList;

public class Admin extends User{
	public Admin(ResultSet rs) throws SQLException{
		super(rs);
		this.user_type = rs.getInt("type_user");
		this.user_id = rs.getInt("admin_id");
	}
	
	public Admin(HttpServletRequest request){
		super(request);
		this.user_type = SVList.ADMIN_USER_TYPE;
	}
}
