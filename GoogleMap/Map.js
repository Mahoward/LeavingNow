var ref = new Firebase("https://leaving-now.firebaseio.com/Users/482331016");
var map;
var marker;

function initialize() {
  var myLatlng = new google.maps.LatLng(36.9741667,-122.0297222

);
  var mapOptions = {
    zoom: 7,
    center: myLatlng
  }
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

  marker = new google.maps.Marker({
      position: myLatlng,
      map: map,
      title: 'Hello World!'
  });
  moveMarker(map, marker);
}

google.maps.event.addDomListener(window, 'load', initialize);



function moveMarker( map, marker ) {
    
    //delayed so you can see it move
    setTimeout( function(){
        marker.setPosition( new google.maps.LatLng( 0, 0 ) );
        map.panTo( new google.maps.LatLng( 0, 0 ) );
        
    }, 1500 );

};

ref.on("child_changed", function(snapshot) {
  var changedPost = snapshot.val();
  console.log("The updated post long is " + changedPost.Long);
});


