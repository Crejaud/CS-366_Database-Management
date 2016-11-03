package com.cs336.obj.auction;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.cs336.stat.SVList;

public class Item {
	public int item_id;
	// item_RAM, item_storage, camera_q
	public int[] item_att_int = new int[3];
	
	public float item_plat_version;
	//dim_x, dim_y, dim_display, dim_thickness, weight, battery_time
	public float[] item_att_float = new float[6];
	
	public String item_name, item_company;
	//description, image
	public String[] item_att_str = new String[2];
	// used to track if this item is a part of wish list

	int final_price;
	
	public Item(ResultSet rs){
		try{
			item_id = rs.getInt("item_id");
			item_name = rs.getString("item_name");
			item_plat_version = rs.getFloat("item_plat_version");
			item_company = rs.getString("item_company");
			
			for(int x=0; x<SVList.item_att_int.length; x++){
				String s = SVList.item_att_int[x];
				// rs.getInt returns 0 if column is null
				item_att_int[x] = rs.getInt(s);
			}
			
			for(int x=0; x<SVList.item_att_float.length; x++){
				String s = SVList.item_att_float[x];
				item_att_float[x] = rs.getFloat(s);
			}
			
			for(int x=0; x<SVList.item_att_str.length; x++){
				String s = SVList.item_att_str[x];
				if(rs.getString(s) != null)
					item_att_str[x] = rs.getString(s);
			}
		}catch(SQLException e){e.printStackTrace();}
	}
	public Item(HttpServletRequest request){
		this.item_name = (String)request.getParameter("title");
		this.item_company = (String)request.getParameter("os");
		this.item_plat_version = Float.parseFloat(request.getParameter("version"));
		for(int x=0; x<SVList.item_param_int.length; x++){
			item_att_int[x] = Math.round(Float.parseFloat(request.getParameter(SVList.item_param_int[x])));
		}
		for(int x=0; x<SVList.item_param_float.length; x++){
			item_att_float[x] = Float.parseFloat(request.getParameter(SVList.item_param_float[x]));
		}
		for(int x=0; x<SVList.item_param_str.length; x++){
			item_att_str[x] = (String)request.getParameter(SVList.item_param_str[x]);
		}
	}
	public Item(ResultSet rs, int wish){
		try{
			item_plat_version = rs.getFloat("phone_plat_version");
			item_company = rs.getString("phone_company");
		}catch(SQLException e){e.printStackTrace();}
	}
	public Item(HttpServletRequest request, int wish){
		this.item_plat_version = Float.parseFloat(request.getParameter("version"));
		this.item_company = (String)request.getParameter("os");
	}
	public Item(ResultSet rs, int sales_report, int summary){
		try{
			item_plat_version = rs.getFloat("item_plat_version");
			item_company = rs.getString("item_company");
		}catch(SQLException e){e.printStackTrace();}
	}
	
	public void changeItem(HttpServletRequest request){
		this.item_name = (String)request.getParameter("title");
		this.item_company = (String)request.getParameter("os");
		this.item_plat_version = Float.parseFloat(request.getParameter("version"));
		for(int x=0; x<SVList.item_param_int.length; x++){
			item_att_int[x] = Math.round(Float.parseFloat(request.getParameter(SVList.item_param_int[x])));
		}
		for(int x=0; x<SVList.item_param_float.length; x++){
			item_att_float[x] = Float.parseFloat(request.getParameter(SVList.item_param_float[x]));
		}
		for(int x=0; x<SVList.item_param_str.length; x++){
			item_att_str[x] = (String)request.getParameter(SVList.item_param_str[x]);
		}
	}
	
	public String getItem_name(){return item_name;}
	public String getItem_company(){return item_company;}
	public float getItem_plat_version(){return item_plat_version;}
	public int[] getItem_att_int(){return item_att_int;}
	public float[] getItem_att_float(){return item_att_float;}
	public String[] getItem_att_str(){return item_att_str;}
	public int getItem_id(){return item_id;}
	
	public boolean equals(Object o){
		if(o == null || !(o instanceof Item))
			return false;
		Item i = (Item)o;
		return i.item_plat_version==this.item_plat_version && i.item_company.equals(this.item_company);
	}
}
