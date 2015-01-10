var SF = new google.maps.LatLng(36.991429, -122.060817)

function calcRoute() {
	var directionsService = new google.maps.DirectionsService();
	var directionsDisplay = new google.maps.DirectionsRenderer();
	directionsService.route(request, function(response, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			directionsDisplay.setDirections(response);
		}
	});
}

function initialize(){
	var	directionsDisplay = new google.maps.DirectionsRenderer();
	var mapProp = {
		center: SF,
		zoom:7,
		panControl:true,
		zoomControl:true,
		mapTypeControl:true,
		scaleControl:true,
		streetViewControl:true,
		overviewMapControl:true,
		rotateControl:true,    
		mapTypeId: google.maps.MapTypeId.HYBRID
	};
 	var contentString = '<div id="content">'+
		'<div id="siteNotice">'+
		'</div>'+
		'<h1 id="firstHeading" class="firstHeading">Uluru</h1>'+
		'<div id="bodyContent">'+
		'<p><b>Uluru</b>, also referred to as <b>Ayers Rock</b>, is a large ' +
		'sandstone rock formation in the southern part of the '+
		'Northern Territory, central Australia. It lies 335&#160;km (208&#160;mi) '+
		'south west of the nearest large town, Alice Springs; 450&#160;km '+
		'(280&#160;mi) by road. Kata Tjuta and Uluru are the two major '+
		'features of the Uluru - Kata Tjuta National Park. Uluru is '+
		'sacred to the Pitjantjatjara and Yankunytjatjara, the '+
		'Aboriginal people of the area. It has many springs, waterholes, '+
		'rock caves and ancient paintings. Uluru is listed as a World '+
		'Heritage Site.</p>'+
		'<p>Attribution: Uluru, <a href="http://en.wikipedia.org/w/index.php?title=Uluru&oldid=297882194">'+
		'http://en.wikipedia.org/w/index.php?title=Uluru</a> '+
		'(last visited June 22, 2009).</p>'+
		'</div>'+
		'</div>'; 

	var map = new google.maps.Map(document.getElementById("googleMap"),mapProp);
	calcRoute()
	function isInfoWindowOpen(infoWindow){
		var map = infoWindow.getMap();
		return (map === null || typeof map === "undefined");
	}
	
	var marker = new google.maps.Marker({
		position:SF,
	});

	//marker.setMap(map);
	directionsDisplay.setMap(map);

	// Zoom to 20 when clicking on marker
	google.maps.event.addListener(marker,'dblclick',function() {
		map.setZoom(20);
		map.setCenter(marker.getPosition());
	});
	
	google.maps.event.addListener(marker,'click',function() {
		infowindow2.open(map, marker);
		if (isInfoWindowOpen(infowindow2)){
			marker.setAnimation(google.maps.Animation.BOUNCE);
		}
	});

	
	marker.setMap(map);
	
	google.maps.event.addListener(marker,'click',function() {
		infowindow.open(map, marker);

	});

	var sanFranControl = new SFControl(homeControlDiv, map);

}
google.maps.event.addDomListener(window, 'load', initialize);



