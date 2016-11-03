package com.cs336.obj.auction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Wish_List {
	ArrayList<Item> wish_list;
	
	public Wish_List(ResultSet rs){
		wish_list = new ArrayList<Item>();
		try {
			while(rs.next()){
				wish_list.add(new Item(rs, 1));
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public ArrayList<Item> getWish_list(){return wish_list;}
}
