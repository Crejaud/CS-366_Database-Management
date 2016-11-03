package com.cs336.dao.usr;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.EndUser;
import com.cs336.usr.User;

/**
 * Servlet implementation class GetUser
 */

public class GetUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGetorPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGetorPost(request, response);
	}
	protected void doGetorPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute(SVList.USER_PARAM);
		String type = (String) session.getAttribute(SVList.TYPE_PARAM);
		
		if(type != null || user != null){
			request.setAttribute(SVList.MSG_PARAM, SVList.LOGIN_SUCCESS);
			request.getRequestDispatcher(SVList.PAGE_CONTROLLER).forward(request, response);
			return;
		}
		
		String username = (String)request.getParameter("user");
		String password = (String)request.getParameter("pass");
		if(username == null || password == null){
			request.setAttribute(SVList.MSG_PARAM, SVList.LOGIN_FAILURE);
			request.getRequestDispatcher(SVList.LOGIN_CONTROLLER).forward(request, response);
			return;
		}
		
		ApplicationsDAO dao = new ApplicationsDAO();
		User u = null;
		try{
			int usertype = dao.GetLogInfo(username, password);
			switch(usertype){
				case 0:
					request.setAttribute(SVList.MSG_PARAM, SVList.LOGIN_FAILURE);
					request.getRequestDispatcher(SVList.LOGIN_CONTROLLER).forward(request, response);
					return;
				case 1:
					u = dao.GetEndUser(username);
					break;
				case 2:
					u = dao.GetCustRep(username);
					break;
				case 3:
					u = dao.GetAdmin(username);
					break;
				default:
					break;
			}
		}finally{
			dao.closeConnection();
		}
		
		if(u == null){
			request.setAttribute(SVList.MSG_PARAM, SVList.LOGIN_FAILURE);
			request.getRequestDispatcher(SVList.LOGIN_CONTROLLER).forward(request, response);
			return;
		}
		
		session.setAttribute(SVList.USER_PARAM, u.user_name);
		session.setAttribute(SVList.TYPE_PARAM, Integer.toString(u.user_type));
		request.setAttribute(SVList.MSG_PARAM, SVList.LOGIN_SUCCESS + " Welcome, " + u.user_name + "!");
		request.getRequestDispatcher(SVList.PAGE_CONTROLLER).forward(request, response);

	}
}
