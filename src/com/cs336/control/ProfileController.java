package com.cs336.control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.obj.ticket.Ticket_List;
import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.EndUser;


public class ProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProfileController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute(SVList.USER_PARAM);
		String type = (String) session.getAttribute(SVList.TYPE_PARAM);
		
		if(type == null || user == null || Integer.parseInt(type) != SVList.END_USER_TYPE){
			request.getRequestDispatcher(SVList.LOGIN_CONTROLLER).forward(request, response);
			return;
		}
		
		ApplicationsDAO dao = new ApplicationsDAO();
		try {	
			/* get Current User */
			EndUser eu = dao.GetEndUser(user);
			eu.grab_Wish_List(dao.Get_Wish_List(eu));
			request.setAttribute(SVList.WISH_PARAM, eu.wl);
		} finally {
			dao.closeConnection();
		}
		
		request.getRequestDispatcher(SVList.PROFILE_PAGE).forward(request, response);
	}

}
