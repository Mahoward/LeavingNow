url = document.URL;
var query = url.split("?");
console.log(query[1]);
var ref = new Firebase("https://leaving-now.firebaseio.com/Users/"+query[1]);

var map;
var marker;
var Longitude;
var Latitude;
var url;




function initialize() {

	ref.child('Long').once('value', function(dataSnapshot) {
		Longitude=parseFloat(dataSnapshot.val());
		ref.child('Lat').once('value', function(dataSnapshot) {
			Latitude=parseFloat(dataSnapshot.val());
			    var myLatlng = new google.maps.LatLng(Latitude,Longitude);
  			    var mapOptions = {
   	 		    	zoom: 18,
    		    	center: myLatlng
  				}
  			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

  			marker = new google.maps.Marker({
      			position: myLatlng,
      			map: map,
      			title: 'Hello World!'
  			});
  			moveMarker(map, marker);
		});
	});
}

google.maps.event.addDomListener(window, 'load', initialize);



function moveMarker( map, marker ) {
    ref.on("child_changed", function(snapshot) {
  		ref.child('Long').once('value', function(dataSnapshot) {
			Longitude=parseFloat(dataSnapshot.val());
			ref.child('Lat').once('value', function(dataSnapshot) {
				Latitude=parseFloat(dataSnapshot.val());
				marker.setPosition( new google.maps.LatLng( Latitude, Longitude ) );
				map.panTo( new google.maps.LatLng( Latitude, Longitude ) );

			});
		});
	});
    //delayed so you can see it move
     //   marker.setPosition( new google.maps.LatLng( 0, 0 ) );
      //  map.panTo( new google.maps.LatLng( 0, 0 ) );
};
