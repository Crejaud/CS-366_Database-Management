<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>View Tickets</title>
 	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  	<%@ page import="com.cs336.stat.SVList" %>
</head>
	<%
		boolean hasMessage;
		String message = "";
		if(request.getAttribute(SVList.MSG_PARAM) == null)
			hasMessage = false;
		else{
			hasMessage = true;
			message = (String)request.getAttribute(SVList.MSG_PARAM);
		}
	%>
<body>
	<nav class="navbar navbar-default">
	  <div class="container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	    <div class="navbar-header">
	      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
	        <span class="sr-only">Toggle navigation</span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <a class="navbar-brand" href="#">YABE</a>
	    </div>
	
	    <!-- Collect the nav links, forms, and other content for toggling -->
	    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	      <!-- <ul class="nav navbar-nav">
	        <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
	        <li><a href="#">Link</a></li>
	        <li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
	          <ul class="dropdown-menu">
	            <li><a href="#">Action</a></li>
	            <li><a href="#">Another action</a></li>
	            <li><a href="#">Something else here</a></li>
	            <li role="separator" class="divider"></li>
	            <li><a href="#">Separated link</a></li>
	            <li role="separator" class="divider"></li>
	            <li><a href="#">One more separated link</a></li>
	          </ul>
	        </li> 
	      </ul> -->
	      <form class="navbar-form navbar-left" role="search">
	        <div class="form-group">
	          <input type="text" class="form-control" placeholder="Search Auctions">
	        </div>
	        <button type="submit" class="btn btn-default">Search</button>
	      </form>
	      <ul class="nav navbar-nav navbar-right">
	        
	        <li><a href="logout">Sign Out</a></li>
	        <!-- <li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
	          <ul class="dropdown-menu">
	            <li><a href="#">Action</a></li>
	            <li><a href="#">Another action</a></li>
	            <li><a href="#">Something else here</a></li>
	            <li role="separator" class="divider"></li>
	            <li><a href="#">Separated link</a></li>
	          </ul>
	        </li> -->
	      </ul>
	    </div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	<div class="container">
		<% if(hasMessage){ %>
			<h1><%=message%></h1>
		<% } %>
		
		<div id="displayAllTickets">
			<table>
				<thead>
					<tr>
						<th>Ticket ID</th>
						<th>EndUser Name</th>
						<th>Ticket Header</th>
						<th>Ticket Body</th>
						<th>Created On</th>
						<th>Is Open</th>
						<th>Answered</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jspTicket.ticket_list}" var="ticket">
					    <tr ticket_id="<c:out value='${ticket.ticket_id}'/>" 
					    class="ticketItem" 
					    <c:if test="${not empty ticket.message_header}">ticket_title="<c:out value='${ticket.message_header}'/>"</c:if>
					    <c:if test="${not empty ticket.message_body}">ticket_description="<c:out value='${ticket.message_body}'/>"</c:if>
					    >
					        <td><c:out value='${ticket.ticket_id}'/></td>
					        <td><c:out value='${ticket.user.user_name}'/></td>
					        <td><c:out value='${ticket.message_header}'/></td>
					        <td><c:out value='${ticket.message_body}'/></td>
					        <td><c:out value='${ticket.ticket_date}'/></td>
					        <td><c:out value='${ticket.is_open}'/></td>
					        <td><c:out value='${ticket.answered}'/></td>
					    </tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

    <button id="show_ticket_button" type="button" style="display: none;" class="btn btn-info btn-lg" data-toggle="modal" data-target="#answerTicketModal">Answer Ticket</button>
    
    <!-- Answer Ticket Modal -->
    <div class="modal fade" id="answerTicketModal" role="dialog">
    	<div class="modal-dialog">
    
	        <!-- Modal content-->
	        <div class="modal-content">
	        	<div class="modal-header">
	          	  <button type="button" class="close" data-dismiss="modal">&times;</button>
	          	  <h4 class="modal-title">Reply to a Ticket!</h4>
	        	</div>
		        <div class="modal-body">
		        	<label>Title: </label>
		        	<input type="text" name="title" id="ticket_title" class="input-xlarge" placeholder="Title" readonly> <br><br>
		            <label>Description: </label>
		            <textarea rows="5" name="description" id="ticket_description" class="input-xlarge" placeholder="Description..." readonly></textarea> <br><br>
		            <form id="answerTicketForm" action="answerticket" method="POST" >
		            	<label>Reply: </label>
		            	<textarea rows="5" name="answer" class="input-xlarge" placeholder="Reply..." required></textarea> <br><br>
		            	<input type="hidden" name="ticket_id" id="ticket_answer_id">
		            </form>
		        </div>
		        <div class="modal-footer">
		          	<input class="btn btn-success" type="submit" value="Reply to Ticket" id="submit" form="answerTicketForm">
		          	<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        </div>
	        </div>
        </div>
    </div>
    
<script>
	// Javascript to generate the select list for platform versions
	
	// This uses JQuery
	
	$(document).ready(function() {
		
		//set up ticket show listener
		setupShowTicket();
		
	});
	
	function setupShowTicket() {
		
		//create onclick listeners for these tikets
		
		// inside listener, post and populate modal, then show it
		var tickets = $(".ticketItem");
		var i = 0;
		var size = tickets.length;
		for (i; i < size; i++) {
			tickets[i].addEventListener("click", function() {
				var ticketID = $(this).attr("ticket_id");
				
				$("#ticket_answer_id").val(ticketID);
				var ticket_title = $(this).attr("ticket_title");
				var ticket_description = $(this).attr("ticket_description");
				
				$("#ticket_title").val(ticket_title);
				$("#ticket_description").val(ticket_description);
				
				/*
				$.post("getTicket", {"ticket_id": ticketID}).done(function(data){
					var ticket_title;
					var ticket_description;
					
					$("#ticket_title").val(ticket_title);
					$("#ticket_description").val(ticket_description);
				});
				*/
				$("#show_ticket_button").click();
			});
		}
	}
	
</script>
</body>
</html>