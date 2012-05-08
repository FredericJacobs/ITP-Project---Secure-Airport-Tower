package messaging;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.messages.RoutingMessage;
import messaging.messages.RoutingMessage.moveType;
import messaging.messages.RoutingMessage.routingMessageType;

public class Circle {
	public static void answerLandingRequest(Plane plane,
			DataOutputStream outData) throws IOException {
		if (Tower.landingRoute.size() == 0) {
			System.out
					.println("Tower tries to give a direct landing instruction");
			Tower.landingRoute.add(plane);
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), Tower.landingPointX,
					Tower.landingPointY, routingMessageType.REPLACEALL,
					moveType.LANDING, int2bytes(0));
			respondLanding.write(outData);
			Event eventR = new Event(respondLanding, "Tower",
					plane.getPlaneID());
			Tower.journal.addEvent(eventR);

		} else if (Tower.smallCircle.size() <= 3) {
			System.out
					.println("Tower tries to give a small circle to the seconde landing plane");
			Tower.smallCircle.add(plane);
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), Tower.smallPointX,
					Tower.smallPointY, routingMessageType.REPLACEALL,
					moveType.CIRCULAR, int2bytes(Tower.smallAngle));
			respondLanding.write(outData);
			Event eventR = new Event(respondLanding, "Tower",
					plane.getPlaneID());
			Tower.journal.addEvent(eventR);
		} else if (Tower.middleCircle.size() <= 10) {
			Tower.middleCircle.add(plane);
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), Tower.middlePointX,
					Tower.middlePointY, routingMessageType.NEWFIRST,
					moveType.CIRCULAR, int2bytes(Tower.middleAngle));
			respondLanding.write(outData);
		} else if (Tower.longCircle.size() <= 100) {
			Tower.longCircle.add(plane);
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.longPointX, Tower.longPointY,
					routingMessageType.NEWFIRST, moveType.CIRCULAR,
					int2bytes(Tower.longAngle));
			RoutingMessage respondLanding2 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.straightX, Tower.straightY,
					routingMessageType.NEWFIRST, moveType.STRAIGHT,
					int2bytes(0));
			RoutingMessage respondLanding3 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.landingPointX,
					Tower.landingPointY, routingMessageType.LAST,
					moveType.LANDING, int2bytes(0));
			respondLanding1.write(outData);
			respondLanding2.write(outData);
			respondLanding3.write(outData);
		} else {
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), Tower.landingPointX,
					Tower.landingPointY, routingMessageType.REPLACEALL,
					moveType.NONE, int2bytes(0));
			respondLanding.write(outData);
		}
	}

	public static void landingUrgent(Plane plane, DataOutputStream outData)
			throws IOException {
		Tower.landingRoute.removeAll(null);
		Tower.landingRoute.add(plane);
		RoutingMessage respondLanding = new RoutingMessage(
				"Tour0000".getBytes(), Tower.landingPointX,
				Tower.landingPointY, routingMessageType.REPLACEALL,
				moveType.LANDING, int2bytes(0));
		respondLanding.write(outData);
		/*
		 * for(int i = 0;i<Tower.planes.length;i++){
		 * answerLandingRequest(Tower.planes[i],outData);
		 */
	}

	// Transfer int to byte[]
	private static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

}
