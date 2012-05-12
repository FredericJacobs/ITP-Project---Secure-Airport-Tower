package Plane;

import java.io.IOException;

import messaging.messages.MayDayMessage;
import generals.XYPosition;
import Plane.PlaneType;

public class Plane implements Runnable {
	private PlaneType type;
	static private XYPosition position;
	private double fuelLevel;
	private double fuelAlertLevel;
	
	Plane() {
		this.type = PlaneType.A320;
		Plane.position = new XYPosition();
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

	static public XYPosition getPosition() {
		return position;
	}

	@Override
	public void run() {
		while (true){
			if (fuelLevel < fuelAlertLevel){
				String alertMessage = "Running low on fuel. Need quick landing";
				MayDayMessage mayDay = new MayDayMessage (TestPlane.getPlaneID(), alertMessage.length(), position.getPosx(), position.getPosy(),alertMessage);
				try {
					mayDay.write(TestPlane.getOut());
				} catch (IOException e) {
					System.exit(-1);
				}
			}
		}
		
	}
	
}
