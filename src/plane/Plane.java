package plane;

import generals.XYPosition;

import java.io.IOException;

import messaging.messages.MayDayMessage;

/**
 * This class is a wrapper class of what a plane can be.
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 **/

public class Plane implements Runnable {
	static private PlaneType type;
	static private XYPosition position;
	private double fuelLevel;
	private double fuelAlertLevel;

	Plane() {
		Plane.type = PlaneType.A320;
		Plane.position = new XYPosition();
		this.fuelLevel = PlaneType.A320.getFuelCapacity();
		this.fuelAlertLevel = PlaneType.A320.getFuelCapacity() * 0.2;
	}

	public void changeTypeTo(PlaneType type) {
		Plane.type = type;
	}

	public boolean setFuelLevel(double fuelLevel) {
		if (fuelLevel < type.getFuelCapacity()) {
			this.fuelLevel = fuelLevel;
			this.fuelAlertLevel = type.getFuelCapacity() * 0.2;
			return true;
		} else {
			return false;
		}
	}

	static public XYPosition getPosition() {
		return position;
	}

	static public void setPosition(XYPosition newPosition) {
		Plane.position = newPosition;
	}

	static public double getPlaneSpeed() {
		return type.getMachSpeed();
	}

	@Override
	public void run() {
		while (true) {
			if (fuelLevel < fuelAlertLevel) {
				String alertMessage = "Running low on fuel. Need quick landing";
				MayDayMessage mayDay = new MayDayMessage(
						TestPlane.getPlaneID(), alertMessage.length(),
						position.getPosx(), position.getPosy(), alertMessage);
				try {
					mayDay.write(TestPlane.getOut());
				} catch (IOException e) {
					System.exit(-1);
				}
			}
		}

	}

}
