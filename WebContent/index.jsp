<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0; padding: 0 }
  #map_canvas { height: 100%; margin-top: 50px;}
</style>
<script type="text/javascript"
    src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDAJrNEEEeiN-_qU	KKhbcLjuCHAkv4VSoU&sensor=true">
</script>
<script type="text/javascript">
  var map;
  
  function initialize() {
    var latlng = new google.maps.LatLng(40.4468314, -79.9479933);
    var myOptions = {
      zoom: 8,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions);

  }

  function timedPing(username, lat, lon){

  t=setTimeout("timedPing()",1000);
  $.getJSON('EventTrackerServlet', function(data) {
	$.each(data, function(i, event) {
        var lat = event.location.lat;
        var lng = event.location.lng;
        var info = event.location.name;
        var id = String(event.id);
        
       addMarker(new google.maps.LatLng(lat, lng), info, id);
	});
  });
  
  }

  function addMarker(location, info, id) {
	  marker = new google.maps.Marker({
	    position: location,
	    map: map,
	    id: id
	  });
	  
	  var infowindow = new google.maps.InfoWindow({
		    content: info
		});
	  
	  google.maps.event.addListener(marker, 'click', function() {
		  infowindow.open(map,marker);
		});
  }
  
/*   function getUserLocation(){
		if(google.loader.ClientLocation)
		{
			lat = google.loader.ClientLocation.latitude;
			lon = google.loader.ClientLocation.longitude;
		}
  }
   */
   
  function addUser(form){
	  console.log(form);
	  var lat = form.lat.value;
	  var lon = form.lon.value;
	  var point = new google.maps.LatLng(form.lat.value, form.lon.value);
	  addMarker(point, "You are here!", "userMarker");
	
	  timedPing(form.username.value, lat, lon);
  }

</script>
	<body onload="initialize()">
		<h1>Event Tracker</h1>
		<!--  String currentUser = (String) request.getAttribute("username");
			if (currentUser == null || currentUser.length() == 0){ -->
		
		<form method="POST" name="userForm" action="">
			<label for="username">Username: </label>
			<input type="text" id="username" name="username"/><br>
			<label for="lat">Lat: </label>
			<input type="text" id="lat" name="lat"/><br>
			<label for="lon">Lon: </label>
			<input type="text" id="lon" name="lon"/><br>

			<input type="button" value="Add to map" onclick="addUser(userForm)"/>
		</form>
		<div id="map_canvas" style="width:500px; height:500px"></div>	
	</body>
</html>