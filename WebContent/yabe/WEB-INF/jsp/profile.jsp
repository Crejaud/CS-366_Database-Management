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
<div class="container">
	<h1>Welcome to <%=message%>'s Profile Page!!!</h1>
	<%
		if(hasMessage){
	%>
			<h5><%=message%></h5>
	<%
		}
	%>

    <!-- Trigger the modal with a button -->
    <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#addWishlistModal">Add to Wishlist!</button>

    <!-- Modal -->
    <div class="modal fade" id="addWishlistModal" role="dialog">
    	<div class="modal-dialog">
    
	        <!-- Modal content-->
	        <div class="modal-content">
	        	<div class="modal-header">
	          	  <button type="button" class="close" data-dismiss="modal">&times;</button>
	          	  <h4 class="modal-title">Add to Wishlist!</h4>
	        	</div>
		        <div class="modal-body">
		            <form id="addWishListForm" action="createwl" method="POST" >
		            	  <select id="os_search" name="os" required>
							<option value="" selected>Select Operating System</option>
			    			<option value="windows">Windows</option>
			    			<option value="android">Android</option>
			    			<option value="ios">iOS</option>
			    			<option value="amazon">Amazon Fire</option>
			  			  </select>
			  			  <select id="os_version_search" name="version" required>
							<option value="" disabled selected>Select Platform Version</option>
								<!-- generate this through javascript -->
			  			  </select>
		            </form>
		        </div>
		        <div class="modal-footer">
		          	<input class="btn btn-success" type="submit" value="Add" id="submit" form="addWishListForm">
		          	<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        </div>
	        </div>
        </div>
    </div>
    
    <div id="displayAllWishlist">
			<table>
				<thead>
					<tr>
						<th>Company</th>
						<th>Version</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jspWish.wish_list}" var="item">
					    <tr>
					        <td><c:out value='${item.item_company}'/></td>
					        <td><c:out value='${item.item_plat_version}'/></td>
					   </tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
    
</div>
<script>
	// Javascript to generate the select list for platform versions
	
	// This uses JQuery
	
	$(document).ready(function() {
		
		//setup search dropdown
		setupSearchDropdown();
		
	});
	
	function setupSearchDropdown() {
		// listen for os_search change
		
		var osSelector = $("#os_search");
		
		osSelector[0].addEventListener("change", function() {
		
			var os = $("#os_search").val();
			
			var versionSelector = $("#os_version_search");
		
			// remove any previous options (except for default)
			
			while(versionSelector.children().length > 1) {
				versionSelector.children().last().remove();
			}
		
			// add the options pertaining to the OS
			if (os === "windows") {
				versionSelector.append($("<option></option>")
					.attr("value", 7.0)
					.text("Windows 7"));
				versionSelector.append($("<option></option>")
					.attr("value", 7.5)
					.text("Windows 7.5"));
				versionSelector.append($("<option></option>")
					.attr("value", 7.8)
					.text("Windows 7.8"));
				versionSelector.append($("<option></option>")
					.attr("value", 8.0)
					.text("Windows 8"));
				versionSelector.append($("<option></option>")
					.attr("value", 8.1)
					.text("Windows 8.1"));
				versionSelector.append($("<option></option>")
					.attr("value", 10.0)
					.text("Windows 10"));
			}
			if (os === "android") {
				versionSelector.append($("<option></option>")
					.attr("value", 1.0)
					.text("APK 1"));
				versionSelector.append($("<option></option>")
					.attr("value", 2.0)
					.text("APK 2"));
				versionSelector.append($("<option></option>")
					.attr("value", 3.0)
					.text("APK 3: Cupcake"));
				versionSelector.append($("<option></option>")
					.attr("value", 4.0)
					.text("APK 4: Donut"));
				versionSelector.append($("<option></option>")
					.attr("value", 5.0)
					.text("APK 5: Eclair"));
				versionSelector.append($("<option></option>")
					.attr("value", 6.0)
					.text("APK 6: Eclair"));
				versionSelector.append($("<option></option>")
					.attr("value", 7.0)
					.text("APK 7: Eclair"));
				versionSelector.append($("<option></option>")
					.attr("value", 8.0)
					.text("APK 8: Froyo"));
				versionSelector.append($("<option></option>")
					.attr("value", 9.0)
					.text("APK 9: Gingerbread"));
				versionSelector.append($("<option></option>")
					.attr("value", 10.0)
					.text("APK 10: Gingerbread"));
				versionSelector.append($("<option></option>")
					.attr("value", 11.0)
					.text("APK 11: Honeycomb"));
				versionSelector.append($("<option></option>")
					.attr("value", 12.0)
					.text("APK 12: Honeycomb"));
				versionSelector.append($("<option></option>")
					.attr("value", 13.0)
					.text("APK 13: Honeycomb"));
				versionSelector.append($("<option></option>")
					.attr("value", 14.0)
					.text("APK 14: Ice Cream Sandwich"));
				versionSelector.append($("<option></option>")
					.attr("value", 15.0)
					.text("APK 15: Ice Cream Sandwich"));
				versionSelector.append($("<option></option>")
					.attr("value", 16.0)
					.text("APK 16: Jelly Bean"));
				versionSelector.append($("<option></option>")
					.attr("value", 17.0)
					.text("APK 17: Jelly Bean"));
				versionSelector.append($("<option></option>")
					.attr("value", 18.0)
					.text("APK 18: Jelly Bean"));
				versionSelector.append($("<option></option>")
					.attr("value", 19.0)
					.text("APK 19: KitKat"));
				versionSelector.append($("<option></option>")
					.attr("value", 20.0)
					.text("APK 20: KitKat"));
				versionSelector.append($("<option></option>")
					.attr("value", 21.0)
					.text("APK 21: Lollipop"));
				versionSelector.append($("<option></option>")
					.attr("value", 22.0)
					.text("APK 22: Lollipop"));
				versionSelector.append($("<option></option>")
					.attr("value", 23.0)
					.text("APK 23: Marshmellow"));
			}
			if (os === "ios") {
				versionSelector.append($("<option></option>")
					.attr("value", 3.13)
					.text("3.1.3"));
				versionSelector.append($("<option></option>")
					.attr("value", 4.21)
					.text("4.2.1"));
				versionSelector.append($("<option></option>")
					.attr("value", 5.11)
					.text("5.1.1"));
				versionSelector.append($("<option></option>")
					.attr("value", 6.16)
					.text("6.1.6"));
				versionSelector.append($("<option></option>")
					.attr("value", 7.12)
					.text("7.1.2"));
				versionSelector.append($("<option></option>")
					.attr("value", 8.41)
					.text("8.4.1"));
				versionSelector.append($("<option></option>")
					.attr("value", 9.3)
					.text("9.3"));
				versionSelector.append($("<option></option>")
					.attr("value", 9.31)
					.text("9.3.1"));
				versionSelector.append($("<option></option>")
					.attr("value", 9.32)
					.text("9.3.2"));
			}
			if (os === "amazon") {
				versionSelector.append($("<option></option>")
					.attr("value", 1.0)
					.text("Version 1"));
			}
		});
	}
	
</script>
</body>
</html>
