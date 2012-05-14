package plane;

import java.io.IOException;

import plane.PlaneType;

import messaging.messages.MayDayMessage;
import generals.XYPosition;

public class Plane implements Runnable {
	static private PlaneType type;
	static private XYPosition position;
	private double fuelLevel;
	private double fuelAlertLevel;
	
	Plane() {
		Plane.type = plane.A320;
		Plane.position = new XYPosition();
		this.fuelLevel = plane.A320.getFuelCapacity();
		this.fuelAlertLevel = plane.A320.getFuelCapacity()*0.2;
	}
		
	public void changeTypeTo (PlaneType type){
		Plane.type = type;
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
	
	static public void setPosition(XYPosition newPosition){
		Plane.position = newPosition;
	}
	
	static public double getPlaneSpeed (){
		return type.getMachSpeed();
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