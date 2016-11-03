package com.cs336.dao.usr;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.EndUser;

public class SetEndUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SetEndUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGetorPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGetorPost(request, response);
	}
	protected void doGetorPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String)request.getParameter("user");
		String password = (String)request.getParameter("pass");

		if(username == null || password == null){
			request.setAttribute(SVList.MSG_PARAM, SVList.CREATION_FAILURE);
			request.getRequestDispatcher(SVList.LOGIN_PAGE).forward(request, response);
			return;
		}
		
		EndUser e = new EndUser(request);
		ApplicationsDAO dao = new ApplicationsDAO();
		try{
			if(dao.SetEndUser(e))
				request.setAttribute(SVList.MSG_PARAM, SVList.CREATION_SUCCESS);
			else
				request.setAttribute(SVList.MSG_PARAM, SVList.CREATION_FAILURE);
		}finally{
			dao.closeConnection();
		}
		
		request.getRequestDispatcher(SVList.LOGIN_PAGE).forward(request, response);
	}

}
