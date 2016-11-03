package com.cs336.dao.ticket;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.obj.ticket.Ticket;
import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.C_Rep;

public class AnswerTicket extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AnswerTicket() {
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
		}else if(Integer.parseInt(type) != SVList.CR_USER_TYPE){
			response.getWriter().write(SVList.UNAUTHORIZED);
			return;
		}

		ApplicationsDAO dao = new ApplicationsDAO();
		try{
			C_Rep cr = dao.GetCustRep(user);
			if(cr == null){
				response.getWriter().write(SVList.USER_NOT_FOUND);
				return;
			}
			
			/* Get the ticket object */
			int ticket_id = Integer.parseInt(request.getParameter("ticket_id"));
			String answer = (String) request.getParameter("answer");

			ResultSet ticketRS = dao.Get_One_Ticket(ticket_id);
			Ticket t = null;
			try {
				if(ticketRS.next())
					 t = new Ticket(ticketRS);
			} catch (SQLException e) {e.printStackTrace();response.getWriter().write(SVList.TICKET_FAILED);return;}
			
			t.cr = cr;
			t.answer = answer;
			
			/* Answer the ticket */
			if(dao.setTicketAnswered(t))
				response.getWriter().write(SVList.TICKET_ANSWERED);
			else
				response.getWriter().write(SVList.TICKET_FAILED);
		}finally{
			dao.closeConnection();
		}
	}

}
