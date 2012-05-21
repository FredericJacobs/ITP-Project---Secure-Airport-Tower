package messaging;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.messages.RoutingMessage;
import messaging.messages.RoutingMessage.moveType;
import messaging.messages.RoutingMessage.routingMessageType;

/**
 * This class is the main part to arrange the landing request of the planes and also send the routing messages
 * to different planes. It has also a method to respond the Mayday message, called the landingUrgent. 
 * 
 * The general idea of designing the routing respond is that we judge which is the closed valid route, and
 * add the current plane to the arraylist of the route.For example if the landing route is valid right now we 
 * will send a landing routing message directly. In this way all the planes will be well arranged. 
 * 
 * @author Hantao Zhao 
 * @author Frederic Jacobs
 * @version 1.0
 *
 */
public class Circle {
	public static void answerLandingRequest(Plane plane,
			DataOutputStream outData) throws IOException {
		// Judge if the landing route is valid 
		if (Tower.landingRoute.size() == 0) {
		// Add the current plane into the arraylist
			Tower.landingRoute.add(plane);
		//Send out the message
			RoutingMessage respondLanding0 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.straightX, Tower.straightY,
					routingMessageType.REPLACEALL, moveType.STRAIGHT,
					int2bytes(0));
			respondLanding0.write(outData);
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), Tower.landingPointX,
					Tower.landingPointY, routingMessageType.LAST,
					moveType.LANDING, int2bytes(0));
			respondLanding.write(outData);
		} 
		// Judge if the smallCircle route is valid 
		else if (Tower.smallCircle.size() < 3) {
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

		}
		// Judge if the middleCircle route is valid 
		else if (Tower.middleCircle.size() < 10) {
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

		}
		// Judge if the longCircle route is valid 
		else if (Tower.longCircle.size() < 100) {
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
	
	// This method aims at handling the mayday plane. 
	public static void landingUrgent(Plane plane, DataOutputStream outData)
			throws IOException {
		// remove the current landing plane and replace it with the may day planes
		Tower.landingRoute.clear();
		Tower.landingRoute.add(plane);

		// The following allows the Mayday-plane to land as soon as possible
		RoutingMessage respondLanding0 = new RoutingMessage(
				"Tour0000".getBytes(), Tower.straightX, Tower.straightY,
				routingMessageType.REPLACEALL, moveType.STRAIGHT,
				int2bytes(0));
		respondLanding0.write(outData);
		RoutingMessage respondLanding = new RoutingMessage(
				"Tour0000".getBytes(), Tower.landingPointX,
				Tower.landingPointY, routingMessageType.LAST,
				moveType.LANDING, int2bytes(0));
		respondLanding.write(outData);
		// Clear the recent circle and arrange them again
		Tower.smallCircle.clear();
		Tower.middleCircle.clear();
		Tower.longCircle.clear();
		for (int i = 0; i < Tower.planes.size(); i++) {
			Plane planelist = Tower.planes.get(i);
			DataOutputStream outDataList;
			try {
				outDataList = new DataOutputStream(planelist.getSocket()
						.getOutputStream());
				Circle.answerLandingRequest(planelist, outDataList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Transfer int to byte[], when we need to send a routingmessage
	public static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

}