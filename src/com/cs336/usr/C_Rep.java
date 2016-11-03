package com.cs336.usr;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.cs336.stat.SVList;

public class C_Rep extends User{
	
	
	public C_Rep(ResultSet rs) throws SQLException{
		super(rs);
		this.user_type = rs.getInt("type_user");
		this.user_id = rs.getInt("cr_id");
	}
	
	public C_Rep(HttpServletRequest request){
		super(request);
		this.user_type = SVList.CR_USER_TYPE;
	}
}
