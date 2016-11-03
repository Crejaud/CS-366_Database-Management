package com.cs336.dao.usr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;
import com.cs336.usr.C_Rep;

public class CreateCRep extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CreateCRep() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute(SVList.USER_PARAM);
		String type = (String) session.getAttribute(SVList.TYPE_PARAM);
		
		if(user == null || type == null || Integer.parseInt(type) != SVList.ADMIN_USER_TYPE){
			response.getWriter().write(SVList.UNAUTHORIZED);
			return;
		}
	
		String username = (String)request.getParameter("user");
		String password = (String)request.getParameter("pass");

		if(username == null || password == null){
			response.getWriter().write(SVList.CREATION_FAILURE);
			return;
		}
		
		C_Rep c = new C_Rep(request);
		ApplicationsDAO dao = new ApplicationsDAO();
		try{
			if(dao.SetCustRep(c))
				response.getWriter().write(SVList.CREATION_SUCCESS);
			else
				response.getWriter().write(SVList.CREATION_FAILURE);
		}finally{
			dao.closeConnection();
		}
	}
}
