package plane;

import generals.XYPosition;

import java.io.IOException;

import messaging.messages.ByeMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.RoutingMessage.moveType;

public class PlaneNavigation implements Runnable {

	double planeSpeed = Plane.getPlaneSpeed();
	static RoutingInstruction currentInstruction = new RoutingInstruction(533,
			437, 0, moveType.STRAIGHT);

	@Override
	public void run() {
		while (true) {
			try {

				movePlane(TestPlane.getPlaneUpdateInterval());

				new KeepAliveMessage(TestPlane.getPlaneID(), Plane
						.getPosition().getPosx(), Plane.getPosition().getPosy())
						.write(PlaneMessaging.getOutputStream());

				Thread.sleep(TestPlane.getPlaneUpdateInterval());

			} catch (InterruptedException e) {

			} catch (IOException e) {

			}
		}

	}

	private void moveInStraightLine(double deltaTimeSeconds,
			RoutingInstruction instruction) {
		// Compute the normalized direction vector (dx, dy)
		double dx = instruction.getxCoord() - Plane.getPosition().getPosx();
		double dy = instruction.getyCoord() - Plane.getPosition().getPosy();
		double speed = planeSpeed / 12;
		double length = Math.sqrt(dy * dy + dx * dx);
		if (length > 0) {
			dx = dx / length;
			dy = dy / length;
		}

		// How much time will it take us to get there?
		double timeNeeded = length / speed;
		double timeTraveled = (timeNeeded > deltaTimeSeconds) ? deltaTimeSeconds
				: timeNeeded;
		// Move the plane.
		double newX = Plane.getPosition().getPosx() + dx * timeTraveled * speed;
		double newY = Plane.getPosition().getPosy() + dy * timeTraveled * speed;

		Plane.getPosition().updatePosition((int) newX, (int) newY);

		// If we arrived, continue with the next road instruction
		if (timeTraveled == timeNeeded) {
			System.out.println("Plane " + TestPlane.getPlaneID().toString()
					+ " arrived at waypoint (" + instruction.getxCoord() + ", "
					+ instruction.getyCoord() + ").");
			try {
				new ByeMessage(TestPlane.getPlaneID(), 0, Plane.getPosition()
						.getPosx(), Plane.getPosition().getPosy())
						.write(PlaneMessaging.getOutputStream());
			} catch (IOException e) {

			}
			System.exit(-1);

		}
	}

	private void moveInCircle(double deltaTimeSeconds,
			RoutingInstruction instruction) {
		// Moves the plane in circle around the given center, with an objective
		// of given angle.
		double modX = Plane.getPosition().getPosx() - instruction.getxCoord();
		double modY = Plane.getPosition().getPosy() - instruction.getyCoord();
		double speed = planeSpeed / 12;
		double instructionAngle = Math.toRadians(instruction.getAngle()); // Angle
																			// en
																			// radians
		double instructionAngleSign = Math.signum(instructionAngle); // Signe de
																		// l'angle
		double r = Math.sqrt(modX * modX + modY * modY); // Rayon
		double theta = computeTheta(modX, modY, 0, 0); //
		double rotSpeed = speed / r; // Vitesse angulaire
		double deltaTheta = rotSpeed * deltaTimeSeconds; // Angle parcouru
		if (deltaTheta < Math.abs(instructionAngle)) {
			theta = theta + instructionAngleSign * deltaTheta;
			modX = (r * Math.cos(theta) + instruction.getxCoord());
			modY = (r * Math.sin(theta) + instruction.getyCoord());
			Plane.setPosition(new XYPosition((int) modX, (int) modY));
		} else {
			theta = theta + instructionAngle;
			modX = (r * Math.cos(theta) + instruction.getxCoord());
			modY = (r * Math.sin(theta) + instruction.getyCoord());
			Plane.setPosition(new XYPosition((int) modX, (int) modY));
			System.out.println("Plane " + TestPlane.getPlaneID().toString()
					+ " arrived at waypoint (" + modX + ", " + modY + ").");
			if (deltaTheta > Math.abs(instructionAngle)) {
				double neededTime = Math.abs(instructionAngle) / rotSpeed;
				movePlane(deltaTimeSeconds - neededTime);
			}
		}
	}

	private void movePlane(double d) {

		switch (currentInstruction.getType()) {
		case STRAIGHT:
		case LANDING:
		case DESTRUCTION: {
			moveInStraightLine(d, currentInstruction);
			break;
		}
		case CIRCULAR: {
			moveInCircle(d, currentInstruction);
			break;
		}
		case NONE: {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		}

	}

	private double computeTheta(double px, double py, int cx, int cy) {
		return Math.atan2(py - cy, px - cx);
	}
}