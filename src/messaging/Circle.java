package messaging;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.messages.RoutingMessage;
import messaging.messages.RoutingMessage.moveType;
import messaging.messages.RoutingMessage.routingMessageType;

public class Circle {
	public static void answerLandingRequest(Plane plane, DataOutputStream outData)
			throws IOException {
		if (Tower.landingRoute.size() == 0) {
			Tower.landingRoute.add(plane);
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.landingPointX,
					Tower.landingPointY, routingMessageType.NEWFIRST,
					moveType.LANDING, int2bytes(0));
			respondLanding.write(outData);
		} else if (Tower.smallCircle.size() <= 3) {
			Tower.smallCircle.add(plane);
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.smallPointX,
					Tower.smallPointY, routingMessageType.NEWFIRST,
					moveType.CIRCULAR, int2bytes(Tower.smallAngle));
			RoutingMessage respondLanding2 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.straightX, Tower.straightY,
					routingMessageType.NEWFIRST, moveType.STRAIGHT,
					int2bytes(0));
			RoutingMessage respondLanding3 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.landingPointX,
					Tower.landingPointY, routingMessageType.LAST,
					moveType.LANDING, int2bytes(0));
			respondLanding1.write(outData);
			respondLanding2.write(outData);
			respondLanding3.write(outData);
		} else if (Tower.middleCircle.size() <= 10) {
			Tower.middleCircle.add(plane);
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.middlePointX,
					Tower.middlePointY, routingMessageType.NEWFIRST,
					moveType.CIRCULAR, int2bytes(Tower.middleAngle));
			RoutingMessage respondLanding2 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.straightX, Tower.straightY,
					routingMessageType.NEWFIRST, moveType.STRAIGHT,
					int2bytes(0));
			RoutingMessage respondLanding3 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.landingPointX,
					Tower.landingPointY, routingMessageType.LAST,
					moveType.LANDING, int2bytes(0));
			respondLanding1.write(outData);
			respondLanding2.write(outData);
			respondLanding3.write(outData);
		}
		else if(Tower.longCircle.size() <= 100){
			Tower.longCircle.add(plane);
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.longPointX,
					Tower.longPointY, routingMessageType.NEWFIRST,
					moveType.CIRCULAR, int2bytes(Tower.longAngle));
			RoutingMessage respondLanding2 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.straightX, Tower.straightY,
					routingMessageType.NEWFIRST, moveType.STRAIGHT,
					int2bytes(0));
			RoutingMessage respondLanding3 = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.landingPointX,
					Tower.landingPointY, routingMessageType.LAST,
					moveType.LANDING, int2bytes(0));
			respondLanding1.write(outData);
			respondLanding2.write(outData);
			respondLanding3.write(outData);
		}
		else{
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), 0, Tower.landingPointX,
					Tower.landingPointY, routingMessageType.REPLACEALL,
					moveType.NONE, int2bytes(0));
			respondLanding.write(outData);
		}
	}
//Transfer int to byte[]
	private static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

}
