package com.cs336.dao.auction;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.obj.auction.Auction;
import com.cs336.obj.auction.Bid;
import com.cs336.obj.auction.Bid_History;
import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.EndUser;

/**
 * Servlet implementation class CreateBid
 */

public class CreateBid extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CreateBid() {
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
			/* Get User Account for this current user */
			EndUser u = dao.GetEndUser(user);
			if(u == null){
				response.getWriter().write(SVList.USER_NOT_FOUND);
				return;
			}
			
			/* Get Auction from auction_id */
			int auction_id = Integer.parseInt((String)request.getParameter("auction_id"));
			Auction a = null;
			if((a=dao.Get_One_Auction(auction_id)) == null){
				response.getWriter().write(SVList.AUCTION_GET_FAILED);
				return;
			}
			
			/* Get Bid_History for this Auction */
			Bid_History bh = new Bid_History(a, dao.Get_Bid_History(a.auction_id));
			
			/* Make a Bid Object */
			Bid b = new Bid(u, request);
			
			/* Make sure Bid is a valid Bid */
			if(!bh.isValidBid(b)){
				response.getWriter().write(SVList.BID_NOT_VALID);
				return;
			}
			
			/* Write the bid into database */
			if(dao.Create_Bid(b, auction_id) >= 0)
				response.getWriter().write(SVList.BID_CREATED);
			else
				response.getWriter().write(SVList.BID_FAILED);
		} catch (SQLException e) {e.printStackTrace();
		}finally{
			dao.closeConnection();
		}
		/* SHOULD WE SEND THE BID_HISTORY BACK? */
	}

}
