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
<div class="container">
	<h1>Welcome to YABE!</h1>
	<%
		if(hasMessage){
	%>
			<h5><%=message%></h5>
	<%
		}
	%>
	<form action="userinf" method="POST">
		<input type="text" name="user" placeholder="Username"><br><br>
		<input type="password" name="pass" placeholder="Password"><br><br>
		<input type="submit" class="btn btn-info btn-lg" value="Sign In">
	</form>
	<br>

    <!-- Trigger the modal with a button -->
    <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#signUpModal">Sign Up</button>

    <!-- Modal -->
    <div class="modal fade" id="signUpModal" role="dialog">
    	<div class="modal-dialog">
    
	        <!-- Modal content-->
	        <div class="modal-content">
	        	<div class="modal-header">
	          	  <button type="button" class="close" data-dismiss="modal">&times;</button>
	          	  <h4 class="modal-title">Sign Up to YABE</h4>
	        	</div>
		        <div class="modal-body">
		            <form id="signUpForm" action="createacc" method="POST" >
		            	<input type="text" name="user" class="input-xlarge" placeholder="Username"><br><br>
		            	<input type="password" name="pass" class="input-xlarge" placeholder="Password"><br><br>
		            	<input type="text" name="first" class="input-xlarge" placeholder="First Name"><br><br>
		            	<input type="text" name="last" class="input-xlarge" placeholder="Last Name"><br><br>
		            	<input type="email" name="email" class="input-xlarge" placeholder="Email"><br>
		            </form>
		        </div>
		        <div class="modal-footer">
		          	<input class="btn btn-success" type="submit" value="Create Account" id="submit" form="signUpForm">
		          	<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        </div>
	        </div>
        </div>
    </div>
</div>
</body>
</html>
