package com.cs336.obj.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cs336.sql.ApplicationsDAO;
import com.cs336.stat.SVList;


public class SendReport extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SendReport() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	/* params: choice
	 * specific params: os // username
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute(SVList.USER_PARAM);
		String type = (String) session.getAttribute(SVList.TYPE_PARAM);
		
		if(user == null || type == null || Integer.parseInt(type) != SVList.ADMIN_USER_TYPE){
			response.getWriter().write(SVList.SESSION_TIMEDOUT);
			return;
		}
		
		String choice = (String) request.getParameter("option");
		System.out.println(choice);
		if(choice == null)
			return;
		JSONObject report = null;

		boolean doOS = false;
		boolean doUser = false;
		
		switch(Integer.parseInt(choice)){
			case 1:
				doUser = true;
				break;
			case 2:
				doOS = true;
				break;
			default:
				response.getWriter().write(SVList.ACTION_FAILED);
				return;
		}
		
		ApplicationsDAO dao = new ApplicationsDAO();
		try{
			if(doUser){
				report = getUserReport(dao, request);
			}
			if(doOS){
				report = getOSReport(dao, request);
			}
		}finally{
			dao.closeConnection();
		}
		
		if(report != null){
			response.setContentType("application/json");
			response.getWriter().write(report.toString());
		}
	}

	private JSONObject getUserReport(ApplicationsDAO dao, HttpServletRequest request) {
		String username = (String)request.getParameter("username");
		if(username == null)
			return null;
		JSONObject value = dao.Get_User_Report(username);
		return value;
	}

	private JSONObject getOSReport(ApplicationsDAO dao, HttpServletRequest request) {
		String company = (String)request.getParameter("os");
		System.out.println(company);
		if(company == null)
			return null;
		JSONObject value = dao.Get_OS_Report(company);
		return value;
	}
}
