function initialize() {
	positionColumn = 4;
	categoryColumn = 1;
	markersArray = [];
	UnSelectedCategories = new Array();
	
	popoverIsDisplayed = false;
	
	var myOptions = {
		center: new google.maps.LatLng(	
		handleData(allPositions);
	}
	
	function handleData(allPositions){
		
		while(allPositions.hasNext()) {
		    console.log(allPositions.next());
		}
		
		
		data = allPosition.table.rows;
		tempData = jQuery.extend(true, {}, data);
		drawpins();	
	}

}

function drawpins() {	
	for (i=0 ; i < data.length ; i++){
		var buffer = data[i];
		for (j	=0; j < UnSelectedCategories.length ; j++){
			if (buffer[categoryColumn] == UnSelectedCategories[j]){
				tempData.splice([i],1);
			}
		}
	}
	
	marker(0);
}


function marker(i){
    if (i > data.length) return;
    var marker;
	positionArray = data[i][positionColumn].split(',');

        markerIcon = new google.maps.MarkerImage("/i-point_clignotant.gif");

	marker = new google.maps.Marker({
        position: new google.maps.LatLng(positionArray[0], positionArray[1]),
		rowid : i,
        map : map,
		icon : markerIcon
        });
	var locationObject = data[i];
	
   	var fn = markerClick(map, marker, locationObject);
   	google.maps.event.addListener(marker, 'click', fn);
	markersArray.push(marker);

}


function markerClick(map, m, locationObject) {
	return function() {
    	popup(locationObject)
	};
}	
