package com.cs336.dao.ticket;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.obj.ticket.Ticket;
import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.EndUser;

public class CreateTicket extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CreateTicket() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
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
			/* Find the User */
			EndUser u = dao.GetEndUser(user);
			/* Make a Ticket Object */
			Ticket t = new Ticket(u, request);
			
			if(dao.Create_Ticket(t) >= 0)
				response.getWriter().write(SVList.TICKET_CREATED);
			else
				response.getWriter().write(SVList.TICKET_FAILED);
		} finally{
			dao.closeConnection();
		}
	}
}
