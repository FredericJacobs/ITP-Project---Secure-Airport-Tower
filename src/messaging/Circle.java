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
			Tower.landingRoute.add(plane);
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), Tower.landingPointX,
					Tower.landingPointY, routingMessageType.REPLACEALL,
					moveType.LANDING, int2bytes(0));
			respondLanding.write(outData);
		} else if (Tower.smallCircle.size() < 3) {
			Tower.smallCircle.add(plane);
			RoutingMessage respondLanding0 = new RoutingMessage(
					"Tour0000".getBytes(), 400, 150,
					routingMessageType.REPLACEALL, moveType.STRAIGHT,
					int2bytes(0));
			respondLanding0.write(outData);
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.smallPointX,
					Tower.smallPointY, routingMessageType.LAST,
					moveType.CIRCULAR, int2bytes(Tower.smallAngle));
			respondLanding1.write(outData);

		} else if (Tower.middleCircle.size() < 10) {
			Tower.middleCircle.add(plane);
			RoutingMessage respondLanding0 = new RoutingMessage(
					"Tour0000".getBytes(), 300, 650,
					routingMessageType.REPLACEALL, moveType.STRAIGHT,
					int2bytes(0));
			respondLanding0.write(outData);
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.middlePointX,
					Tower.middlePointY, routingMessageType.LAST,
					moveType.CIRCULAR, int2bytes(Tower.middleAngle));
			respondLanding1.write(outData);

		} else if (Tower.longCircle.size() < 100) {
			Tower.longCircle.add(plane);
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), 800, 300,
					routingMessageType.REPLACEALL, moveType.STRAIGHT,
					int2bytes(0));
			RoutingMessage respondLanding2 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.longPointX, Tower.longPointY,
					routingMessageType.LAST, moveType.CIRCULAR,
					int2bytes(Tower.longAngle));
			respondLanding1.write(outData);
			respondLanding2.write(outData);
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
	public static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

}
