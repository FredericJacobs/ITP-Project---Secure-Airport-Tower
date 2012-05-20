package messaging;

import generals.*;

public class PlanePosition {
	private String planeid;
	private XYPosition position;
	boolean hasCrashed;
	boolean sentMayDay;
	
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
	
	public void didCrash(boolean status){
		hasCrashed = status;
	}
	
	public boolean hasCrashed () {
		return hasCrashed;
	}
	
	public void setMayDayStatus(boolean MayDayStatus){
		sentMayDay = MayDayStatus;
	}
	
	public boolean sentMayDay(){
		return sentMayDay;
	}
	
}
