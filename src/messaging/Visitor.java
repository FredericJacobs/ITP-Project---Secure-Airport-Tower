package messaging;

import java.io.DataOutputStream;
import java.io.IOException;
import GUI.AirportGUI;
import messaging.messages.*;

public class Visitor {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Event eventR = new Event(respondHelloMessage, "Tower",
					message.getPlaneID());
			Tower.journal.addEvent(eventR);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Event eventR = new Event(respondHelloMessage, "Tower",
					message.getPlaneID());
			Tower.journal.addEvent(eventR);
			plane.setInitialTime(System.currentTimeMillis());
			return 0;
		}

	}

	public int visit(Plane plane, DataMessage message, DataOutputStream outData) {
		return 0;
	}

	public int visit(Plane plane, MayDayMessage message,
			DataOutputStream outData) {
		System.out.println("Try to handle the mayday message");
		AirportGUI.choker.chokeEnabled(true);
		try {
			Circle.landingUrgent(plane, outData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;// Mayday
	}

	public int visit(Plane plane, SendRSAMessage message,
			DataOutputStream outData) {
		return 2;
	}

	public int visit(Plane plane, ChokeMessage message, DataOutputStream outData) {
		return 0;
	}

	public int visit(Plane plane, UnchokeMessage message,
			DataOutputStream outData) {
		return 0;
	}

	public int visit(Plane plane, ByeMessage message, DataOutputStream outData) {
		return 0;
	}

	public int visit(Plane plane, RoutingMessage message,
			DataOutputStream outData) {
		Tower.landingRoute.remove(0);
		ByeMessage respondHelloMessage = new ByeMessage("Tour0000".getBytes(),
				0, 0, 0);
		try {
			respondHelloMessage.write(outData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int visit(Plane plane, KeepAliveMessage message,
			DataOutputStream outData) {
		plane.setPosx(((KeepAliveMessage) message).keepaliveX());
		plane.setPosy(((KeepAliveMessage) message).keepaliveY());
		return 0;
	}

	public int visit(Plane plane, LandingMessage message,
			DataOutputStream outData) {
		try {
			Circle.answerLandingRequest(plane, outData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
