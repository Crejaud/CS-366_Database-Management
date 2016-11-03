<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html SYSTEM "about:legacy-compat">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Home</title>
 	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  	<%@ page import="com.cs336.stat.SVList, com.cs336.obj.auction.*" %>
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
	      	<li><a href="profile">View Profile</a></li>
	      
	        <!-- Trigger the Create Auction modal with a button -->
			<li><button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#createAuctionModal">Create Auction</button></li>
	        
	        <!-- Trigger the Create Ticket modal with a button -->
			<li><button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#createTicketModal">Create Ticket</button></li>
	        
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
		
	        <div class="form-group">
	          <input id="title_search" type="text" class="form-control" placeholder="Search Auctions By Title">
	          <select id="os_search" name="os">
				<option value="" selected>Select Operating System</option>
    			<option value="windows">Windows</option>
    			<option value="android">Android</option>
    			<option value="ios">iOS</option>
    			<option value="amazon">Amazon Fire</option>
  			  </select>
  			  <select id="os_version_search" name="version">
				<option value="" selected>Select Platform Version</option>
					<!-- generate this through javascript -->
  			  </select>
			  <select id="ram_search" name="ram">
				<option value="" selected>Select RAM</option>
    			<option value="1">1 GB</option>
    			<option value="2">2 GB</option>
    			<option value="4">4 GB</option>
    			<option value="8">8 GB+</option>
  			  </select>
			  <select id="storage_search" name="storage">
				<option value="" selected>Select Storage</option>
    			<option value="1">1 GB</option>
    			<option value="2">2 GB</option>
    			<option value="4">4 GB</option>
				<option value="8">8 GB</option>
    			<option value="16">16 GB</option>
    			<option value="32">32 GB</option>
    			<option value="64">64 GB</option>
				<option value="128">128 GB+</option>
  			  </select>
	        </div>
	        <button onclick="searchAuctions()" class="btn btn-default">Search</button>
		
		<div id="displayAllAuctions">
			<table>
				<thead>
					<tr>
						<th>Item Name</th>
						<th>Item Company</th>
						<th>Item Version</th>
						<th>RAM</th>
						<th>Storage</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jspAuction.auction_list}" var="auction">
						<c:set var="bid_list" value=""/>
						<c:forEach var="bid" items="${auction.bid_history.bids}">
						    <c:set var="bid_list" value="${bid_list},${bid.bid_amount}"/>
						</c:forEach>
					    <tr id="<c:out value='${auction.auction_id}'/>" 
					    class="auctionItem" 
					    <c:if test="${not empty auction.item.item_name}">auction_title="<c:out value='${auction.item.item_name}'/>"</c:if>
					    <c:if test="${not empty auction.public_increment}">auction_bid_increment="<c:out value='${auction.public_increment}'/>"</c:if>
					    <c:if test="${not empty auction.bid_history.winningBid.bid_amount}">auction_highest_bid="<c:out value='${auction.bid_history.winningBid.bid_amount}'/>" </c:if>
					    <c:if test="${not empty auction.bid_history.winningBid.buyer.user_name}">auction_highest_bidder="<c:out value='${auction.bid_history.winningBid.buyer.user_name}'/>"</c:if>
					    <c:if test="${not empty auction.end_date}">auction_enddate="<c:out value='${auction.end_date}'/>" </c:if> 
					    <c:if test="${not empty auction.item.item_company}">auction_os="<c:out value='${auction.item.item_company}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_plat_version}">auction_version="<c:out value='${auction.item.item_plat_version}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_int[0]}">auction_ram="<c:out value='${auction.item.item_att_int[0]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_int[1]}">auction_storage="<c:out value='${auction.item.item_att_int[1]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_float[0]}">auction_x_dim="<c:out value='${auction.item.item_att_float[0]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_float[1]}">auction_y_dim="<c:out value='${auction.item.item_att_float[1]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_float[2]}">auction_display_dim="<c:out value='${auction.item.item_att_float[2]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_float[3]}">auction_thickness="<c:out value='${auction.item.item_att_float[3]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_float[4]}">auction_weight="<c:out value='${auction.item.item_att_float[4]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_float[5]}">auction_battery_time="<c:out value='${auction.item.item_att_float[5]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_int[2]}">auction_cam_quality="<c:out value='${auction.item.item_att_int[2]}'/>" </c:if>
					    <c:if test="${not empty auction.item.item_att_str[0]}">auction_description="<c:out value='${auction.item.item_att_str[0]}'/>" </c:if>
					    <c:if test="true">bid_history="${bid_list}" </c:if>
					    >
					        <td><c:out value='${auction.item.item_name}'/></td>
					        <td><c:out value='${auction.item.item_company}'/></td>
					        <td><c:out value='${auction.item.item_plat_version}'/></td>
					        <td><c:out value='${auction.item.item_att_int[0]}'/></td>
					        <td><c:out value='${auction.item.item_att_int[1]}'/></td>
					    </tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>
	
    <!-- Create Auction Modal -->
    <div class="modal fade" id="createAuctionModal" role="dialog">
    	<div class="modal-dialog">
    
	        <!-- Modal content-->
	        <div class="modal-content">
	        	<div class="modal-header">
	          	  <button type="button" class="close" data-dismiss="modal">&times;</button>
	          	  <h4 class="modal-title">Create an Auction!</h4>
	        	</div>
		        <div class="modal-body">
		            <form id="createAuctionForm" action="createauc" method="POST" >
		            	<label>Title: </label>
		            	<input type="text" name="title" class="input-xlarge" placeholder="Enter Title" required> *<br><br>
						<label>Minimum Secret Price: </label>
						<input type="number" name="min_secret" min="1" step="1" placeholder="Enter the secret minimum bid" required> *<br><br>
						<label>Public Bid Increment: </label>
						<input type="number" name="bid_increment" min="1" step="1" placeholder="Enter the public bid increment" required> *<br><br>
						<label>End Date: </label>
						<input id="end_date_input" type="datetime-local" name="end_date" placeholder="End Date" required> *<br><br>
						<label>Operating System: </label>
						<select id="os_select" name="os" required>
							<option value="" disabled selected>Select Operating System</option>
    						<option value="windows">Windows</option>
    						<option value="android">Android</option>
    						<option value="ios">iOS</option>
    						<option value="amazon">Amazon Fire</option>
  						</select> *
  						<br><br>
  						<label>Platform Version: </label>
						<select id="os_version_select" name="version" required>
							<option value="" disabled selected>Select Platform Version</option>
							<!-- generate this through javascript -->
  						</select> *
  						<br><br>
  						<label>RAM: </label>
						<select name="ram" required>
							<option value="" disabled selected>Select RAM</option>
    						<option value="1">1 GB</option>
    						<option value="2">2 GB</option>
    						<option value="4">4 GB</option>
    						<option value="8">8 GB+</option>
  						</select> *
  						<br><br>
  						<label>Storage: </label>
						<select name="storage" required>
							<option value="" disabled selected>Select Storage</option>
    						<option value="1">1 GB</option>
    						<option value="2">2 GB</option>
    						<option value="4">4 GB</option>
							<option value="8">8 GB</option>
    						<option value="16">16 GB</option>
    						<option value="32">32 GB</option>
    						<option value="64">64 GB</option>
							<option value="128">128 GB+</option>
  						</select> *
  						<br><br>
  						<label>X Dimension: </label>
		            	<input type="number" name="dim_x" class="input-xlarge" min="0.1" step="0.1" placeholder="Enter X Dimension" required> Inches*<br><br>
						<label>Y Dimension: </label>
						<input type="number" name="dim_y" class="input-xlarge" min="0.1" step="0.1" placeholder="Enter Y Dimension" required> Inches*<br><br>
		            	<label>Display Dimension: </label>
		            	<input type="number" name="dim_display" class="input-xlarge" min="0.1" step="0.1" placeholder="Enter Display Dimension" required> Inches*<br><br>
		            	<label>Thickness: </label>
		            	<input type="number" name="dim_thickness" class="input-xlarge" min="0.1" step="0.1" placeholder="Enter Thickness" required> Inches*<br><br>
		            	<label>Weight: </label>
		            	<input type="number" name="weight" class="input-xlarge" min="0.1" step="0.1"placeholder="Enter Weight" required> Pounds*<br><br>
		            	<label>Battery Time: </label>
		            	<input type="number" name="battery" class="input-xlarge" min="0.1" step="0.1" placeholder="Enter Battery Time" required> Hours*<br><br>
		            	<label>Camera Quality: </label>
		            	<input type="number" name="cam_quality" class="input-xlarge" min="1" step="1" placeholder="Enter Camera Quality" required> MegaPixels*<br><br>
		            	<label>Description: </label>
		            	<textarea rows="5" name="description" class="input-xlarge" placeholder="Description..."></textarea><br><br>
		            </form>
		        </div>
		        <div class="modal-footer">
		          	<input class="btn btn-success" type="submit" value="Create Auction" id="submit" form="createAuctionForm">
		          	<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        </div>
	        </div>
        </div>
    </div>
   
    <!-- Create Ticket Modal -->
    <div class="modal fade" id="createTicketModal" role="dialog">
    	<div class="modal-dialog">
    
	        <!-- Modal content-->
	        <div class="modal-content">
	        	<div class="modal-header">
	          	  <button type="button" class="close" data-dismiss="modal">&times;</button>
	          	  <h4 class="modal-title">Create a Ticket!</h4>
	        	</div>
		        <div class="modal-body">
		            <form id="createTicketForm" action="createticket" method="POST" >
		            	<input type="text" name="title" class="input-xlarge" placeholder="Title" required> *<br><br>
		            	<textarea rows="5" name="description" class="input-xlarge" placeholder="Description..." required></textarea> *<br><br>
		            </form>
		        </div>
		        <div class="modal-footer">
		          	<input class="btn btn-success" type="submit" value="Submit Ticket" id="submit" form="createTicketForm">
		          	<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        </div>
	        </div>
        </div>
    </div>
    
    <button id="show_auction_button" type="button" style="display: none;" class="btn btn-info btn-lg" data-toggle="modal" data-target="#showAuctionModal">Show Auction</button>
    
    <!-- View Auction Modal -->
    <div class="modal fade" id="showAuctionModal" role="dialog">
    	<div class="modal-dialog">
    
	        <!-- Modal content-->
	        <div class="modal-content">
	        	<div class="modal-header">
	          	  <button type="button" class="close" data-dismiss="modal">&times;</button>
	          	  <h4 class="modal-title">Place a bid!</h4>
	        	</div>
		        <div class="modal-body">
		        
		            <span>Title: <span id="auction_title"/></span> <br>
		            <span>Highest Bid: <span id="auction_highest_bid"/></span> <br>
		            <span>Highest Bidder: <span id="auction_highest_bidder"/></span> <br>
		            <span>Minimum Bid Increment: <span id="auction_bid_increment"/></span> <br>
		            <span>End Date: <span id="auction_enddate"/></span> <br>
		            <span>Operating System: <span id="auction_os"/></span> <br>
		            <span>Platform Version: <span id="auction_version"/></span> <br>
		            <span>RAM (GB): <span id="auction_ram"/></span> <br>
		            <span>Storage (GB): <span id="auction_storage"/></span> <br>
		            <span>X Dimension (Inches): <span id="auction_x_dim"/></span> <br>
		            <span>Y Dimension (Inches): <span id="auction_y_dim"/></span> <br>
		            <span>Display Dimension (Inches): <span id="auction_display_dim"/></span> <br>
		            <span>Thickness (Inches): <span id="auction_thickness"/></span> <br>
		            <span>Weight (Pounds): <span id="auction_weight"/></span> <br>
		            <span>Battery Time (Hours): <span id="auction_battery_time"/></span> <br>
		            <span>Camera Quality (MegaPixels): <span id="auction_cam_quality"/></span> <br>
		            <span>Description: <span id="auction_description"/></span> <br>
		            
		            <table id="bid_history_table">
		            	<thead>
		            		<tr>
		            			<th>Bid History</th>
		            		</tr>
		            	</thead>
		            	<tbody>
		            		<!-- Generate this through Javascript -->
		            	</tbody>
		            </table>
		            <!-- <table>
							<thead>
								<tr>
									<th>Bidder</th>
									<th>Bid</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${jspBidHistory.bidhistory_list}" var="bid">
								    <tr>
								        <td><c:out value='${bid.bidder}'/></td>
								        <td><c:out value='${bid.bid}'/></td>
								   </tr>
							</c:forEach>
						</tbody>
					</table> -->
		        
		        	<br><br><br>
		        
		            <form id="placeBidForm" action="createbid" method="POST" >
		            	<input type="number" name="bid" min="1" step="1" class="input-xlarge" placeholder="Bid Amount" required> *<br><br>
		            	<input type="number" name="upper_limit" min="1" step="1" class="input-xlarge" placeholder="Upper Limit"><br><br>
		            	<input type="hidden" name="auction_id" id="bid_auction_id">
		            </form>
		        </div>
		        <div class="modal-footer">
		          	<input class="btn btn-success" type="submit" value="Place Bid" id="submit" form="placeBidForm">
		          	<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        </div>
	        </div>
        </div>
    </div>
<script>
	// Javascript to generate the select list for platform versions
	
	// This uses JQuery
	
	$(document).ready(function() {
		
		//setup search dropdown
		setupSearchDropdown();
		
		//setup create auction modal
		setupCreateAuction();
		
		//set up auction show listener
		setupShowAuction();
		
		
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
	
	function searchAuctions() {
		// get all search parameters
		var titleSearch = $('#title_search').val();
		var osSearch = $('#os_search').val();
		var versionSearch = $('#os_version_search').val();
		var ramSearch = $('#ram_search').val();
		var storageSearch = $('#storage_search').val();
		
		if (!osSearch)
			osSearch = "";
		if (!versionSearch)
			versionSearch = "";
		if (!ramSearch)
			ramSearch = "";
		if (!storageSearch)
			storageSearch = "";
		
		var auctions = $('.auctionItem');

		// hide all auctions
		auctions.each(function(index, domElement) {
		    var $auction = $(domElement);
			$auction.hide();
		});
		
		// go through the elements and find the one within search results
		auctions.each(function(index, domElement) {
		    var $auction = $(domElement);
		    
			if ($auction.attr("auction_title").indexOf(titleSearch) > -1
				&& $auction.attr("auction_os").indexOf(osSearch) > -1
				&& $auction.attr("auction_version").indexOf(versionSearch) > -1
				&& $auction.attr("auction_ram").indexOf(ramSearch) > -1
				&& $auction.attr("auction_storage").indexOf(storageSearch) > -1) {
				console.log("good!");
				$auction.show();
			}
		});
	}
	
	function setupShowAuction() {
		
		//create onclick listeners for these auctions
		
		// inside listener, post and populate modal, then show it
		var auctions = $(".auctionItem");
		var i = 0;
		var size = auctions.length;
		for (i; i < size; i++) {
			auctions[i].addEventListener("click", function() {
				var auctionID = $(this).attr("id");
				
				$("#bid_auction_id").val(auctionID);
				var auction_title = $(this).attr("auction_title");
				var auction_highest_bid = $(this).attr("auction_highest_bid");
				var auction_highest_bidder = $(this).attr("auction_highest_bidder");
				var auction_bid_increment = $(this).attr("auction_bid_increment");
				var auction_enddate = $(this).attr("auction_enddate");
				var auction_os = $(this).attr("auction_os");
				var auction_version = $(this).attr("auction_version");
				var auction_ram = $(this).attr("auction_ram");
				var auction_storage = $(this).attr("auction_storage");
				var auction_x_dim = $(this).attr("auction_x_dim");
				var auction_y_dim = $(this).attr("auction_y_dim");
				var auction_display_dim = $(this).attr("auction_display_dim");
				var auction_thickness = $(this).attr("auction_thickness");
				var auction_weight = $(this).attr("auction_weight");
				var auction_battery_time = $(this).attr("auction_battery_time");
				var auction_cam_quality = $(this).attr("auction_cam_quality");
				var auction_description = $(this).attr("auction_description");
				var bid_history_list = $(this).attr("bid_history");
				
				console.log(bid_history_list);
				
				var bids = bid_history_list.split(",");
				
				var i = 0;
				for (i; i < bids.length; i++) {
					console.log(bids[i]);
				}
				
				// remove all children
				var bid_history_body = $("#bid_history_table").find('tbody');
				while(bid_history_body.children().length > 0) {
					bid_history_body.children().last().remove();
				}
			
				// add all bids as rows
				for (i=1; i < bids.length; i++) {
					$("#bid_history_table").find('tbody')
				    .append($('<tr>')
				        .append($('<td>')
				        	.text(bids[i])
				        )
				    );
				}
				
				$("#auction_title").html(auction_title);
				$("#auction_highest_bid").html(auction_highest_bid);
				$("#auction_highest_bidder").html(auction_highest_bidder);
				$("#auction_bid_increment").html(auction_bid_increment);
				$("#auction_enddate").html(auction_enddate);
				$("#auction_os").html(auction_os);
				$("#auction_version").html(auction_version);
				$("#auction_ram").html(auction_ram);
				$("#auction_storage").html(auction_storage);
				$("#auction_x_dim").html(auction_x_dim);
				$("#auction_y_dim").html(auction_y_dim);
				$("#auction_display_dim").html(auction_display_dim);
				$("#auction_thickness").html(auction_thickness);
				$("#auction_weight").html(auction_weight);
				$("#auction_battery_time").html(auction_battery_time);
				$("#auction_cam_quality").html(auction_cam_quality);
				$("#auction_description").html(auction_description);
				/*
				$.post("getAuction", {"auction_id": auctionID}).done(function(data){
					$("#auction_title").html();
					$("#auction_price").html();
					$("#auction_highest_bid").html();
					$("#auction_os").html();
					$("#auction_version").html();
					$("#auction_ram").html();
					$("#auction_storage").html();
					$("#auction_x_dim").html();
					$("#auction_y_dim").html();
					$("#auction_display_dim").html();
					$("#auction_thickness").html();
					$("#auction_weight").html();
					$("#auction_battery_time").html();
					$("#auction_cam_quality").html();
					$("#auction_description").html();
				});
				*/
				

				$("#show_auction_button").click();
			});
		}
	}
	
	function setupCreateAuction() {
		// get today's date and time
		var datetime = new Date().toJSON().slice(0,19)
		
		// Set minimum of end-date to be today
		$("#end_date_input").attr({
			"min" : datetime
		});
		
		// listen for os_input change
		
		var osSelector = $("#os_select");
		
		osSelector[0].addEventListener("change", function() {
		
			var os = $("#os_select").val();
			
			var versionSelector = $("#os_version_select");
		
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