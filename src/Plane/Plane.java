package Plane;

import messaging.messages.MayDayMessage;
import generals.XYPosition;
import Plane.PlaneType;

public class Plane implements Runnable {
	private PlaneType type;
	private XYPosition position;
	private double fuelLevel;
	private double fuelAlertLevel;
	
	Plane() {
		this.type = PlaneType.A320;
		this.position = new XYPosition();
		this.fuelLevel = PlaneType.A320.getFuelCapacity();
		this.fuelAlertLevel = PlaneType.A320.getFuelCapacity()*0.2;
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

	@Override
	public void run() {
		while (true){
			if (fuelLevel < fuelAlertLevel){
				String alertMessage = "Running low on fuel. Need quick landing";
				String planeID = "B1778000";
				MayDayMessage mayDay = new MayDayMessage (planeID.getBytes(), alertMessage.length(), position.getPosx(), position.getPosy(),alertMessage);
			}
		}
		
	}
	
}
