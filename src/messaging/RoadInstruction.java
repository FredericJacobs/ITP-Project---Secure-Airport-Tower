package messaging;


//For the plnae to calculate 
public class RoadInstruction {

	public void getxCoord() {

	}

	public void getyCoord() {

	}

	private void moveInStraightLine(double deltaTimeSeconds,
			RoadInstruction instruction) {
		// Compute the normalized direction vector (dx, dy)
		double dx = instruction.getxCoord() - roadMap.getCurrentPosx();
		double dy = instruction.getyCoord() - roadMap.getCurrentPosy();
		double length = Math.sqrt(dy * dy + dx * dx);

		if (length > 0) {

			dx = dx / length;
			dy = dy / length;

		}
		// How much time will it take us to get there?
		double timeNeeded = length / engine.getSpeed();
		double timeTraveled = (timeNeeded > deltaTimeSeconds) ? deltaTimeSeconds
				: timeNeeded;
		// Move the plane.
		double newX = roadMap.getCurrentPosx() + dx * timeTraveled
				* engine.getSpeed();
		double newY = roadMap.getCurrentPosy() + dy * timeTraveled
				* engine.getSpeed();
		roadMap.setNewPlanePosition(newX, newY);
		// If we arrived, continue with the next road instruction
		if (timeTraveled == timeNeeded) {

			System.out.println("Plane " + planeId + " arrived at waypoint ("
					+ instruction.getxCoord() + ", " + instruction.getyCoord()
					+ ").");
			roadMap.removeFirstInstruction();
			movePlane(1000 * (deltaTimeSeconds - timeTraveled));

		}
	}

	private void moveInCircle(double deltaTimeSeconds, RoadInstruction current) {
		// Moves the plane in circle around the given center, with anobjective
		// of given angle.
		RotationInstruction instruction = (RotationInstruction) current;
		double modX = roadMap.getCurrentPosx() - instruction.getxCoord();
		double modY = roadMap.getCurrentPosy() - instruction.getyCoord();
		double instructionAngle = Math.toRadians(instruction.getAngle());
		double instructionAngleSign = Math.signum(instructionAngle);
		double r = Math.sqrt(modX * modX + modY * modY);
		double theta = computeTheta(modX, modY, 0, 0);
		double rotSpeed = engine.getSpeed() / r;
		double deltaTheta = rotSpeed * deltaTimeSeconds;
		if (deltaTheta < Math.abs(instructionAngle)) {
			theta = theta + instructionAngleSign * deltaTheta;
			roadMap.removeFirstInstruction();
			roadMap.addNewFirstRoadPoint(new RotationInstruction(instruction.getxCoord(), instruction.getyCoord(), Math
					.toDegrees(instructionAngle - instructionAngleSign
							* deltaTheta)));
			modX = (int) (r * Math.cos(theta) + instruction.getxCoord());
			modY = (int) (r * Math.sin(theta) + instruction.getyCoord());
			roadMap.setNewPlanePosition(modX, modY);
		} else {
			theta = theta + instructionAngle;
			roadMap.removeFirstInstruction();
			modX = (int) (r * Math.cos(theta) + instruction.getxCoord());
			modY = (int) (r * Math.sin(theta) + instruction.getyCoord());
			roadMap.setNewPlanePosition(modX, modY);
			System.out.println("Plane " + planeId + " arrived at waypoint ("
					+ modX + ", " + modY + ").");
			if (deltaTheta > Math.abs(instructionAngle)) {
				double neededTime = Math.abs(instructionAngle) / rotSpeed;
				movePlane(1000 * (deltaTimeSeconds - neededTime));
			}
		}
	}

	private double computeTheta(double px, double py, double cx, double cy) {

		return Math.atan2(py - cy, px - cx);

	}
}