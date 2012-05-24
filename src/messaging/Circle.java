package messaging;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.messages.RoutingMessage;
import messaging.messages.RoutingMessage.moveType;
import messaging.messages.RoutingMessage.routingMessageType;

/**
 * This class is the main part to arrange the landing request of the planes and
 * also send the routing messages to different planes. It has also a method to
 * respond the Mayday message, called the landingUrgent.
 * 
 * The general idea of designing the routing respond is that we judge which is
 * the closed valid route, and add the current plane to the arraylist of the
 * route.For example if the landing route is valid right now we will send a
 * landing routing message directly. In this way all the planes will be well
 * arranged.
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 * 
 */
public class Circle {
	/**
	 * This method will answer the landing request of the plane, by judging the closest and valid route and sending
	 * out the routing messages. 
	 * @param plane The corresponded plane
	 * @param outData The plane's socket's DataOutputStream
	 * @throws IOException
	 */
	public static void answerLandingRequest(Plane plane,
			DataOutputStream outData) throws IOException {

		// Judge if the landing route is valid
		if (Tower.getInstance().getLandingRoute().size() == 0) {
			// Add the current plane into the arraylist
			Tower.getInstance().getLandingRoute().add(plane);
			// Send out the message. Firstly go to the begin of the tunnel , by using a straight commend
			RoutingMessage respondLanding0 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.getInstance().getStraightX(),
					Tower.getInstance().getStraightY(),
					routingMessageType.REPLACEALL, moveType.STRAIGHT,
					int2bytes(0));
			respondLanding0.write(outData);
			// Then go to the landing tunnel
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.getInstance()
							.getLandingPointX(), Tower.getInstance()
							.getLandingPointY(), routingMessageType.LAST,
					moveType.LANDING, int2bytes(0));
			respondLanding1.write(outData);
			// Add the event to the journal
			Event eventR = new Event(respondLanding1, "Tower",
					plane.getPlaneID());
			Tower.getInstance().getJournal().addEvent(eventR);
		}
		// Judge if the smallCircle route is valid
		else if (Tower.getInstance().getSmallCircle().size() < 3) {
			Tower.getInstance().getSmallCircle().add(plane);
			// Send out the message. Firstly go to the begin of the circle , by using a straight commend
			RoutingMessage respondLanding0 = new RoutingMessage(
					"Tour0000".getBytes(), 400, 150,
					routingMessageType.REPLACEALL, moveType.STRAIGHT,
					int2bytes(0));
			respondLanding0.write(outData);
			// Then enter the small circle
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(),
					Tower.getInstance().getSmallPointX(), Tower.getInstance()
							.getSmallPointY(), routingMessageType.LAST,
					moveType.CIRCULAR, int2bytes(Tower.getInstance()
							.getSmallAngle()));
			respondLanding1.write(outData);
			// Add the event to the journal
			Event eventR = new Event(respondLanding1, "Tower",
					plane.getPlaneID());
			Tower.getInstance().getJournal().addEvent(eventR);
		}
		// Judge if the middleCircle route is valid
		else if (Tower.getInstance().getMiddleCircle().size() < 10) {
			Tower.getInstance().getMiddleCircle().add(plane);
			// Send out the message. Firstly go to the begin of the circle , by using a straight commend
			RoutingMessage respondLanding0 = new RoutingMessage(
					"Tour0000".getBytes(), 300, 650,
					routingMessageType.REPLACEALL, moveType.STRAIGHT,
					int2bytes(0));
			respondLanding0.write(outData);
			// Then enter the middle circle
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.getInstance()
							.getMiddlePointX(), Tower.getInstance()
							.getMiddlePointY(), routingMessageType.LAST,
					moveType.CIRCULAR, int2bytes(Tower.getInstance()
							.getMiddleAngle()));
			respondLanding1.write(outData);
			// Add the event to the journal
			Event eventR = new Event(respondLanding1, "Tower",
					plane.getPlaneID());
			Tower.getInstance().getJournal().addEvent(eventR);
		}
		// Judge if the longCircle route is valid
		else if (Tower.getInstance().getLongCircle().size() < 100) {
			Tower.getInstance().getLongCircle().add(plane);
			// Send out the message. Firstly go to the begin of the circle , by using a straight commend
			RoutingMessage respondLanding0 = new RoutingMessage(
					"Tour0000".getBytes(), 800, 300,
					routingMessageType.REPLACEALL, moveType.STRAIGHT,
					int2bytes(0));
			respondLanding0.write(outData);
			// Then enter the middle circle
			RoutingMessage respondLanding1 = new RoutingMessage(
					"Tour0000".getBytes(), Tower.getInstance().getLongPointX(),
					Tower.getInstance().getLongPointY(),
					routingMessageType.LAST, moveType.CIRCULAR, int2bytes(Tower
							.getInstance().getLongAngle()));
			respondLanding1.write(outData);
			// Add the event to the journal
			Event eventR = new Event(respondLanding0, "Tower",
					plane.getPlaneID());
			Tower.getInstance().getJournal().addEvent(eventR);
		} else {
			RoutingMessage respondLanding = new RoutingMessage(
					"Tour0000".getBytes(), Tower.getInstance()
							.getLandingPointX(), Tower.getInstance()
							.getLandingPointY(), routingMessageType.REPLACEALL,
					moveType.NONE, int2bytes(0));
			respondLanding.write(outData);
		}
	}

	/**
	 *  This method aims at handling the Mayday plane with crisis. 
	 * @param plane The Mayday plane with situation
	 * @param outData The Mayday plane's socket's DataOutputStream
	 * @throws IOException
	 */
	public static void landingUrgent(Plane plane, DataOutputStream outData)
			throws IOException {
		// remove the current landing plane and replace it with the may day planes
		
		Tower.getInstance().getLandingRoute().clear();
		Tower.getInstance().getLandingRoute().add(plane);

		// The following allows the Mayday-plane to land as soon as possible
		
		// Send out the message. Firstly go to the begin of the tunnel , by using a straight commend
		RoutingMessage respondLanding0 = new RoutingMessage(
				"Tour0000".getBytes(), Tower.getInstance().getStraightX(),
				Tower.getInstance().getStraightY(),
				routingMessageType.REPLACEALL, moveType.STRAIGHT, int2bytes(0));
		respondLanding0.write(outData);
		
		// Then go to the landing tunnel
		RoutingMessage respondLanding1 = new RoutingMessage(
				"Tour0000".getBytes(), Tower.getInstance().getLandingPointX(),
				Tower.getInstance().getLandingPointY(),
				routingMessageType.LAST, moveType.LANDING, int2bytes(0));
		respondLanding1.write(outData);
		// Clear the recent circle and arrange them again
		Tower.getInstance().getSmallCircle().clear();
		Tower.getInstance().getMiddleCircle().clear();
		Tower.getInstance().getLongCircle().clear();
		// Give the new instructions to every other planes except the mayday plane. 
		for (int i = 0; i < Tower.getInstance().getPlanes().size(); i++) {
			Plane planelist = Tower.getInstance().getPlanes().get(i);
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

	// Transfer int to byte[], when we need to send a routing message
	public static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

}