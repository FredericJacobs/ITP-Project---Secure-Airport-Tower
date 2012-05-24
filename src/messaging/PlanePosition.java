package messaging;

import generals.XYPosition;

/**
 ** This class allows to have a wrapper object for an Array of Planes with an
 * associated position, crash status and landing status.
 * 
 ** @author Hantao Zhao
 ** @author Frederic Jacobs
 **/

public class PlanePosition {
	private String planeid;
	private XYPosition position;
	boolean hasCrashed;
	boolean sentMayDay;

	PlanePosition(String planeid, XYPosition position) {
		this.planeid = planeid;
		this.position = position;
		hasCrashed = false;
	}

	public String getPlaneID() {
		return this.planeid;
	}

	public void updatePosition(XYPosition position) {
		this.position = position;
	}

	public XYPosition getPosition() {
		return position;
	}

	public void didCrash(boolean status) {
		hasCrashed = status;
	}

	public boolean hasCrashed() {
		return hasCrashed;
	}

	public void setMayDayStatus(boolean MayDayStatus) {
		sentMayDay = MayDayStatus;
	}

	public boolean sentMayDay() {
		return sentMayDay;
	}

}
