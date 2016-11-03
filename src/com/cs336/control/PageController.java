package com.cs336.control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.obj.auction.Auction;
import com.cs336.obj.auction.Auction_List;
import com.cs336.obj.auction.Bid;
import com.cs336.obj.auction.Bid_History;
import com.cs336.obj.sales.Sales_Report;
import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.EndUser;

public class PageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public PageController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute(SVList.USER_PARAM);
		String type = (String) session.getAttribute(SVList.TYPE_PARAM);
		
		boolean doAuctions = false;
		boolean doWishList = false;
		boolean doStats = false;
		String forwardPage = "";
		switch(Integer.parseInt(type)){
			case 1:
				doAuctions = true;
				doWishList = true;
				forwardPage = SVList.ENDUSER_CONTROLLER;
				break;
			case 2:
				doAuctions = true;
				forwardPage = SVList.CR_CONTROLLER;
				break;
			case 3:
				doStats = true;
				forwardPage = SVList.ADMIN_CONTROLLER;
				break;
			default:
				forwardPage = SVList.LOGIN_CONTROLLER;
				break;
		}
		
		
		ApplicationsDAO dao = new ApplicationsDAO();
		/* load Auction list */
		try {
			Auction_List al; 
			if(doAuctions){
				al = new Auction_List(dao.Get_Active_Auctions());
				
				EndUser eu = null;
				if(doWishList){
					eu = dao.GetEndUser(user);
					eu.grab_Wish_List(dao.Get_Wish_List(eu));
				}
				
				for(Auction a: al.auction_list){
					try {
						if(doWishList)
							a.set_Wish_List(eu.wl);
						a.set_Bid_History(new Bid_History(a, dao.Get_Bid_History(a.auction_id)));
					} catch (SQLException e) {e.printStackTrace();return;}
				}
				request.setAttribute(SVList.AUCTION_PARAM, al);
			}else if(doStats){
				Sales_Report sr = new Sales_Report(dao.Prepare_Summary_Report());
				sr.setBestSellingEndUser(dao.Get_Best_Selling_EndUser());
				sr.setBestSellingItem(dao.Get_Best_Selling_Item());
				request.setAttribute(SVList.SALES_PARAM, sr);
			}
		} catch (SQLException e) {e.printStackTrace();
		} finally {
			dao.closeConnection();
		}
		
		request.getRequestDispatcher(forwardPage).forward(request, response);
	}

}
