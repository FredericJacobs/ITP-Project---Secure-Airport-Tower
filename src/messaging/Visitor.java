package messaging;

import java.io.DataOutputStream;
import java.io.IOException;
import GUI.AirportGUI;
import messaging.messages.*;

/**
 * This class is the main part of the visitor pattern. It aims at handling
 * different kinds of messages, such as Hello , Keepalive and so on.Each time
 * there is a message which needs to be handled in the towermessage handle it
 * will call the visit() method so that the Visitor class will judge the type of
 * diffferent message and do different move to respond.
 ** 
 * @author Hantao Zhao
 ** @author Frederic Jacobs
 * 
 */
public class Visitor {
	// Respond to the Hello message
	public int visit(Plane plane, HelloMessage message, DataOutputStream outData) {
		if (((HelloMessage) message).isCrypted()) {// To see if the hello is
			// crypted or not, then
			// give different
			// respond hello message
			HelloMessage respondHelloMessage = new HelloMessage(
					"Tour0000".getBytes(), 0, 0, (byte) (1 << 4));
			try {
				respondHelloMessage.write(outData);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Event eventR = new Event(respondHelloMessage, "Tower",
					message.getPlaneID());
			Tower.getInstance().getJournal().addEvent(eventR);
			plane.setInitialTime(System.currentTimeMillis());
			return 1;
		}

		else {
			HelloMessage respondHelloMessage = new HelloMessage(
					"Tour0000".getBytes(), 0, 0, (byte) 0);
			plane.setPlaneID(message.getPlaneID());
			try {
				respondHelloMessage.write(outData);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Event eventR = new Event(respondHelloMessage, "Tower",
					message.getPlaneID());
			Tower.getInstance().getJournal().addEvent(eventR);
			plane.setInitialTime(System.currentTimeMillis());
			return 0;
		}

	}
	// Respond to the date message, which has been done in the tower message handler
	public int visit(Plane plane, DataMessage message, DataOutputStream outData) {
		return 0;
	}
	// Respond to the MayDayMessage message
	public int visit(Plane plane, MayDayMessage message,
			DataOutputStream outData) {
		System.out.println("Try to handle the mayday message");
		
		Tower.getInstance().getPlanes().remove(plane);// To make sure the mayday plane is an exception now. 
		
		AirportGUI.choker.chokeEnabled(true);// Run the choke mode
		try {
			Circle.landingUrgent(plane, outData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Event eventR = new Event(message, "Tower",
				message.getPlaneID());
		Tower.getInstance().getJournal().addEvent(eventR);
		return 0;
	}
	// Respond to the SendRSAMessage 
	public int visit(Plane plane, SendRSAMessage message,
			DataOutputStream outData) {
		return 2;
	}
	// Respond to the ChokeMessage , wont happen to the tower
	public int visit(Plane plane, ChokeMessage message, DataOutputStream outData) {
		return 0;
	}
	// Respond to the UnChokeMessage , wont happen to the tower
	public int visit(Plane plane, UnchokeMessage message,
			DataOutputStream outData) {
		return 0;
	}
	// Respond to the bye message, which has been done in the tower message handler
	public int visit(Plane plane, ByeMessage message, DataOutputStream outData) {

		return 0;
	}
	// Respond to the routing message , wont happen to the tower
	public int visit(Plane plane, RoutingMessage message,
			DataOutputStream outData) {
		return 0;
	}
	// Respond to the KeepAliveMessage
	public int visit(Plane plane, KeepAliveMessage message,
			DataOutputStream outData) {
		plane.setPosx(((KeepAliveMessage) message).keepaliveX());
		plane.setPosy(((KeepAliveMessage) message).keepaliveY());
		return 0;
	}
	// Respond to the LandingMessage
	public int visit(Plane plane, LandingMessage message,
			DataOutputStream outData) {
		try {
			Circle.answerLandingRequest(plane, outData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
