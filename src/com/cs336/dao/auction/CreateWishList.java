package com.cs336.dao.auction;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.obj.auction.Item;
import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.EndUser;

public class CreateWishList extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CreateWishList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute(SVList.USER_PARAM);
		String type = (String) session.getAttribute(SVList.TYPE_PARAM);
		
		if(type == null || user == null){
			// Javascript has to ALERT this out
			response.getWriter().write(SVList.SESSION_TIMEDOUT);
			return;
		}
		
		ApplicationsDAO dao = new ApplicationsDAO();
		
		try{	
			/* User inputs 1 wish_list item at a time, so we can just make a simple item and put that in sql */
			/* Get User Object */
			EndUser u = dao.GetEndUser(user);
			if(u == null){
				response.getWriter().write(SVList.USER_NOT_FOUND);
				return;
			}
			
			/* Make the Wish List Item */
			Item i = new Item(request, 1);
			
			if(dao.Create_Wish_List_Item(u, i) <= 0){
				response.getWriter().write(SVList.ACTION_FAILED);
				return;
			}
			
			response.getWriter().write(SVList.ACTION_SUCCESS);
		}finally{dao.closeConnection();}
	}

}
