<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>YABE</title>
	
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
	<h1>Welcome Admin!</h1>
	<%
		if(hasMessage){
	%>
			<h5><%=message%></h5>
	<%
		}
	%>
	
    <!-- Trigger the modal with a button -->
    <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#createCRModal">Create Customer Representative</button>

    <br><br><br>

    <!-- Modal -->
    <div class="modal fade" id="createCRModal" role="dialog">
    	<div class="modal-dialog">
    
	        <!-- Modal content-->
	        <div class="modal-content">
	        	<div class="modal-header">
	          	  <button type="button" class="close" data-dismiss="modal">&times;</button>
	          	  <h4 class="modal-title">Create Customer Representative</h4>
	        	</div>
		        <div class="modal-body">
		            <form id="createCRForm" action="createcr" method="POST" >
		            	<input type="text" name="user" class="input-xlarge" placeholder="Username"><br><br>
		            	<input type="password" name="pass" class="input-xlarge" placeholder="Password"><br><br>
		            	<input type="text" name="first" class="input-xlarge" placeholder="First Name"><br><br>
		            	<input type="text" name="last" class="input-xlarge" placeholder="Last Name"><br><br>
		            	<input type="email" name="email" class="input-xlarge" placeholder="Email"><br>
		            </form>
		        </div>
		        <div class="modal-footer">
		          	<input class="btn btn-success" type="submit" value="Create Account" id="submit" form="createCRForm">
		          	<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        </div>
	        </div>
        </div>
    </div>
    
    <div id="displaySalesReport">
			<table cellspacing="400">
				<thead>
					<tr>
						<th>Total Sales</th>
						<th>Best Selling EndUser</th>
						<th>EndUser sales</th>
						<th>Best Selling Item Version</th>
						<th>Best Selling Item Company</th>
						<th>Item sales</th>
					</tr>
				</thead>
				<tbody>
				    <tr id="<c:out value='${jspSales.total_earnings}'/>" 
				    class="salesReport">
				        <td><c:out value='${jspSales.total_earnings}'/></td>
				        <td><c:out value='${jspSales.bestSellingUserID}'/></td>
				        <td><c:out value='${jspSales.userID_earnings}'/></td>
				        <td><c:out value='${jspSales.bestSellingItem.item_plat_version}'/></td>
				        <td><c:out value='${jspSales.bestSellingItem.item_company}'/></td>
				        <td><c:out value='${jspSales.item_earnings}'/></td>
				    </tr>
				</tbody>
			</table>
		</div>
		
	<br><br><br>
		
	<div id="otherStats">
			  <select id="search_option" name="option">
    			<option value="1" selected>Search by User</option>
    			<option value="2">Search by Operating System</option>
  			  </select>
			  <input id="user_search" type="text" class="form-control" placeholder="Enter User">
	          <select id="os_search" name="os">
    			<option value="windows" selected>Windows</option>
    			<option value="android">Android</option>
    			<option value="ios">iOS</option>
    			<option value="amazon">Amazon Fire</option>
  			  </select><br>
  			  <button id="search_other_btn" onclick="searchStats()" type="button" class="btn btn-info btn-lg">Search</button>
  			  <h3 id="otherStatOutput"/>
	</div>
</div>
<script>
//Javascript to generate the select list for platform versions

// This uses JQuery

$(document).ready(function() {
	
	//setup search dropdown
	setupSearchDropdown();
	
	
});

function setupSearchDropdown() {
	// listen for os_search change
	// listen for os_search change
		
	var optionSelector = $("#search_option");
	
	$("#user_search").show();
	$("#os_search").hide();
		
	optionSelector[0].addEventListener("change", function() {
		
		var op = $("#search_option").val();
		
		if (op == 1) {
			$("#user_search").show();
			$("#os_search").hide();
		}
		if (op == 2) {
			$("#user_search").hide();
			$("#os_search").show();
		}
		
		// remove any previous options (except for default)
	});
		
}

function searchStats() {
	var op = $("#search_option").val();
	var user = $("#user_search").val();
	var os = $("#os_search").val();
	
	console.log(op);
	console.log(user);
	console.log(os);
	
	$.post("getStats", {"option":op, "os":os, "username":user}).done(function(data){
		console.log(data);
		$("#otherStatOutput").html("Total earnings for " + data.mark + " is: " + data.total);
		});
}
</script>
</body>
</html>
