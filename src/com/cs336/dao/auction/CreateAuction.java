package com.cs336.dao.auction;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.obj.auction.Auction;
import com.cs336.obj.auction.Item;
import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.EndUser;

/**
 * Servlet implementation class CreateAuction
 */

public class CreateAuction extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CreateAuction() {
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
		/* Make an Item object */
		Item i = new Item(request);
		
		ApplicationsDAO dao = new ApplicationsDAO();
		try{
			if((i.item_id = dao.Create_Item(i)) == -1){
				response.getWriter().write(SVList.ITEM_FAILED);
				return;
			}

			/* Get User Object */
			EndUser u = dao.GetEndUser(user);
			if(u == null){
				response.getWriter().write(SVList.USER_NOT_FOUND);
				return;
			}
			
			/* Make an Auction object */
			Auction a = new Auction(u, i, request);
			
			/* Try to insert auction into database */
			if(dao.Create_Auction(a) >= 0)
				response.getWriter().write(SVList.AUCTION_CREATED);
			else
				response.getWriter().write(SVList.AUCTION_FAILED);
		}finally{
			dao.closeConnection();
		}
	}

}
