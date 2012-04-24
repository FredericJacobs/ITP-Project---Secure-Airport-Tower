package messaging;

import generals.*;

public class PlanePosition {
	private String planeid;
	private XYPosition position;
	boolean hasCrashed;
	
	PlanePosition (String planeid, XYPosition position){
		this.planeid = planeid ;
		this.position = position;
		hasCrashed = false;
	}
	
	public String getPlaneID (){
		return this.planeid;
	}
	
	public void updatePosition (XYPosition position){
		this.position = position;
	}
	
	public XYPosition getPosition (){
		return position;
	}
	
	// NO SETTER FOR CRASHED PLANE YET !
	public boolean hasCrashed () {
		return hasCrashed;
	}
	
}
