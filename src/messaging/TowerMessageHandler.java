package messaging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import DataFile.DataFile;

import messaging.messages.DataMessage;
import messaging.messages.HelloMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.Message;
import messaging.messages.RoutingMessage;
import messaging.messages.RoutingMessage.moveType;
import messaging.messages.RoutingMessage.routingMessageType;

/**
 * This class help the Tour to handle different type of messages
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class TowerMessageHandler {
	
	DataFile towerDataFile = null;
	
	public TowerMessageHandler() {
	}

	/**
	 * The respond method,it will respond a message and output the necessary
	 * information by sending it into the DataOutputStream
	 * 
	 * @param plane
	 *            The corresponded plane
	 * @param planenumber
	 * @param message
	 *            The message that need to be handled
	 * @param outData
	 *            The DataOutputStream where we send the feed back message
	 * @throws IOException
	 */
	public int respond(Plane plane, int planenumber, Message message,
			DataOutputStream outData) throws IOException {
		int type = message.getType();
		Event event = new Event(message, message.getPlaneID(), "Tower");
		Tower.journal.addEvent(event);
		plane.setPlaneID(message.getPlaneID());
		switch (type) {// depends on different type of message we go to
						// different cases
		case 0:
			if (((HelloMessage) message).isCrypted()) {// To see if the hello is
														// crypted or not, then
														// give different
														// respond hello message
				HelloMessage respondHelloMessage = new HelloMessage(
						"Tour0000".getBytes(), 0, 0, (byte) (1 << 4));
				respondHelloMessage.write(outData);
				Event eventR = new Event(respondHelloMessage, "Tower",
						message.getPlaneID());
				Tower.journal.addEvent(eventR);
				return 1;
			}

			else {
				HelloMessage respondHelloMessage = new HelloMessage(
						"Tour0000".getBytes(), 0, 0, (byte) 0);
				plane.setPlaneID(message.getPlaneID());
				respondHelloMessage.write(outData);
				Event eventR = new Event(respondHelloMessage, "Tower",
						message.getPlaneID());
				Tower.journal.addEvent(eventR);
				return 0;
			}

		case 1:
			System.out.println("Gotcha Part");
			if (towerDataFile == null){
				towerDataFile = new DataFile ("testfile", (DataMessage) message);
				return 0;
			}
	//		towerDataFile.writePacket((DataMessage) message);
			
			return 0;
			
		case 2:
			Circle.landingUrgent(plane,outData);
			return 0;// Mayday
		case 3:// SendRSA, unfinished for the keypair
			Tower.planes[planenumber].setKeypair(message.getPublicKey());
			return 2;
			// case 4,5,7 shouldnt happen to the tour
		case 6:
			
			if (Tower.landingRoute.size() != 0) {
				Tower.landingRoute.remove(0);
			}	
			System.out.println("Connection terminated");
			return 0;
		case 8:
			plane.setPosx(((KeepAliveMessage) message).keepaliveX());
			plane.setPosy(((KeepAliveMessage) message).keepaliveY());
			return 0;
		case 9:// Landing request
			// Handle the landing message
			Circle.answerLandingRequest(plane, outData);
			return 0;
		default:
			return 0;
		}
	}

}
