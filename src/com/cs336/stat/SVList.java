package com.cs336.stat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SVList {
	/* USER TYPES */
	public static final int END_USER_TYPE = 1;
	public static final int CR_USER_TYPE = 2;
	public static final int ADMIN_USER_TYPE = 3;
	/* JSP */
	public static final String JSP_FILES = "WEB-INF/jsp";
	public static final String LOGIN_PAGE = JSP_FILES + "/login.jsp";
	public static final String ENDUSER_PAGE = JSP_FILES + "/endUser.jsp";
	public static final String CR_PAGE = JSP_FILES + "/cusRep.jsp";
	public static final String ADMIN_PAGE = JSP_FILES + "/admin.jsp";
	public static final String TICKET_PAGE = JSP_FILES + "/tickets.jsp";
	public static final String PROFILE_PAGE = JSP_FILES + "/profile.jsp";
	
	/* Log In */
	public static final String LOGIN_SUCCESS = "Successful Log In!";
	public static final String LOGIN_FAILURE = "No such username and password combination exists. Try creating a new account!";
	public static final String LOGIN_INVALID = "invalid login attempt";
	
	public static final String LOGOUT_SUCCESS = "Successfully Logged Out!";
	
	public static final String CREATION_SUCCESS = "Your account has been created!";
	public static final String CREATION_FAILURE = "Your account could not be created.";
	
	/* Actions */
	public static final String SESSION_TIMEDOUT = "Please log in to take this action.";
	public static final String UNAUTHORIZED = "You are not authorized to do this";
	public static final String USER_NOT_FOUND = "Error - User cannot be found. Please contact your administrator";
	public static final String AUCTION_CREATED = "Your auction has been successfully created!";
	public static final String AUCTION_FAILED = "Failed to create auction";
	public static final String TICKET_CREATED = "Your ticket has been created!";
	public static final String TICKET_FAILED = "Failed ticket action";
	public static final String TICKET_ANSWERED = "The ticket has been answered";
	public static final String ITEM_FAILED = "Failed to create item";
	public static final String AUCTION_GET_FAILED = "Failed to get Auction. It's possible that auction has expired.";
	
	public static final String ACTION_FAILED = "Failed requested action";
	public static final String ACTION_SUCCESS = "Success!";
	
	public static final String BID_CREATED = "Your bid has been accepted!";
	public static final String BID_FAILED = "Your bid has failed to register.";
	public static final String BID_NOT_VALID = "Failed to add Bid, please check the bid amount";
	/* SERVLET PARAM */
		/* jsp */
	public static final String MSG_PARAM = "jspMsg";
	public static final String AUCTION_PARAM = "jspAuction";
	public static final String TICKET_PARAM = "jspTicket";
	public static final String WISH_PARAM = "jspWish";
	public static final String SALES_PARAM = "jspSales";
		/* account */
	public static final String USER_PARAM = "user";
	public static final String TYPE_PARAM = "type";
	
	
	/* SERVLET MAPPING */
	
	public static final String LOGIN_CONTROLLER = "/login";
	public static final String PAGE_CONTROLLER = "/page";
	public static final String ENDUSER_CONTROLLER = "/enduser";
	public static final String CR_CONTROLLER = "/custrep";
	public static final String ADMIN_CONTROLLER = "/admin";
	
	/* DATE */
	public final static DateFormat dateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
	
	/* ITEM */
	public final static String[] item_att_int = {"item_RAM", "item_storage", "camera_q"};
	public final static String[] item_param_int = {"ram", "storage", "cam_quality"};
	public final static String[] item_att_float = {"dim_x", "dim_y", "dim_display", "dim_thickness", "weight", "battery_time"};
	public final static String[] item_param_float = {"dim_x", "dim_y", "dim_display", "dim_thickness", "weight", "battery"};
	public final static String[] item_att_str = {"description", "image"};
	public final static String[] item_param_str = {"description"};
	
}
