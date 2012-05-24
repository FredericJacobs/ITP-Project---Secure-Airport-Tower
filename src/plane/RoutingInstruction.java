package plane;

import generals.XYPosition;
import messaging.messages.RoutingMessage.moveType;

/** Just a wrapper class of a RoutingInstruction
 * @author Frederic Jacobs
 * @author Hantao Zhao
 */
public class RoutingInstruction {

	private moveType planeMoveType;
	private double degrees;
	private XYPosition waypoint;

	public RoutingInstruction(int getxCoord, int getyCoord, double degrees,
			moveType planeMoveType) {
		waypoint = new XYPosition(getxCoord, getyCoord);
		this.degrees = degrees;
		this.planeMoveType = planeMoveType;
	}

	public int getxCoord() {
		return waypoint.getPosx();
	}

	public int getyCoord() {
		return waypoint.getPosy();
	}

	public moveType getType() {
		return planeMoveType;
	}

	public double getAngle() {
		return degrees;
	}

}
