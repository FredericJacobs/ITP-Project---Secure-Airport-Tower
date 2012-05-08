package Plane;

import generals.XYPosition;
import Plane.PlaneType;

public class Plane {
	private PlaneType type;
	private XYPosition position;
	private double fuelLevel;
	
	Plane() {
		this.type = PlaneType.A320;
		this.position = new XYPosition();
		this.fuelLevel = PlaneType.A320.getFuelCapacity();
	}
		
	public void changeTypeTo (PlaneType type){
		this.type = type;
	}
	
	public boolean setFuelLevel (double fuelLevel){
		if (fuelLevel < type.getFuelCapacity()){
			this.fuelLevel = fuelLevel;
			return true;
		}
		else {
			return false;
		}
	}

	public XYPosition getPosition() {
		return position;
	}
	
}
