package com.cs336.dao.auction;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.obj.auction.Auction;
import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;

public class UpdateAuction extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public UpdateAuction() {
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
		}else if(Integer.parseInt(type) != 1){
			response.getWriter().write(SVList.UNAUTHORIZED);
			return;
		}
		
		ApplicationsDAO dao = new ApplicationsDAO();
		try{
			/* Get the Auction */
			int auction_id = Integer.parseInt((String)request.getParameter("auction_id"));
			Auction a = dao.Get_One_Auction(auction_id);
			
			// make sure - I need the auction id here, or else this can't work, along with ALL
			// other attributes as 'create auction'
			
			/* check for the flag - 0 if update, 1 if delete */
			int flag = Integer.parseInt((String) request.getParameter("flag"));
			if(flag == 0)
				if(!doDelete(a, dao)){
					response.getWriter().write(SVList.ACTION_FAILED + " - Auction Deletion");
					return;
				}
			else
				if(!doUpdate(a, dao, request)){
					response.getWriter().write(SVList.ACTION_FAILED + " - Auction Update");
					return;
				}
		}finally{
			dao.closeConnection();
		}
		
		response.getWriter().write(SVList.ACTION_SUCCESS);
	}

	private boolean doUpdate(Auction a, ApplicationsDAO dao, HttpServletRequest request) {
		// user of the auction cannot change
		// a.changeAuction will also call item.changeItem
		a.changeAuction(request);
		
		// change the data in database
		if(!dao.Update_Item(a.item))
			return false;
		if(!dao.Update_Auction(a))
			return false;
		
		return true;
	}

	private boolean doDelete(Auction a, ApplicationsDAO dao) {
		return dao.Delete_Auction(a);
	}

}
