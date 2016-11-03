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


public class TicketController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TicketController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String type = (String) session.getAttribute(SVList.TYPE_PARAM);
		
		if(type == null || Integer.parseInt(type) != SVList.CR_USER_TYPE){
			request.getRequestDispatcher(SVList.LOGIN_CONTROLLER).forward(request, response);
			return;
		}
		
		ApplicationsDAO dao = new ApplicationsDAO();
		/* load Ticket list */
		try {
			Ticket_List tl = new Ticket_List(dao.Get_Opened_Tickets());
			
			request.setAttribute(SVList.TICKET_PARAM, tl);
		} catch (SQLException e) {e.printStackTrace();
		} finally {
			dao.closeConnection();
		}
		
		request.getRequestDispatcher(SVList.TICKET_PAGE).forward(request, response);
	}

}
