package messaging;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.messages.HelloMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.Message;

/**
 * This class help the Tour to handle different type of messages
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class TowerMessageHandler {
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
	public int respond(Plane plane, int planenumber, Message message, DataOutputStream outData)
			throws IOException {
		int type = message.getType();
		switch (type) {// depends on different type of message we go to different cases 
		case 0:
			if (((HelloMessage) message).isCrypted()) {// To see if the hello is crypted or not, then give different respond hello message
				plane.setPlaneID(message.getPlaneID());
				new HelloMessage("Tour0000".getBytes(), 0, 0, (byte) (1 << 4)).write(outData);
				return 1;
			}

			else {
				HelloMessage respondHelloMessage = new HelloMessage(
						"Tour0000".getBytes(), 0, 0, (byte) 0);
				plane.setPlaneID(message.getPlaneID());
				respondHelloMessage.write(outData);
				return 0;
			}
			

		case 1:
			
			System.out.println("got one message");
			return 0;

		// Data, save the file that received TDB
		case 2:
			
			System.out.println("got case 2");
			
			return 0;// Mayday, future issue
		case 3:// SendRSA, unfinished for the keypair
			Tower.planes[planenumber].setKeypair(message.getPublicKey());
			return 2;
		// case 4,5,7 shouldnt happen to the tour
		case 6:
			System.out.println("Connection terminated");
			return 0;
		case 8:
			plane.setPosx(((KeepAliveMessage) message).keepaliveX());
			plane.setPosy(((KeepAliveMessage) message).keepaliveY());
			return 0;
		// keep alive
		case 9:
			System.out
					.println("To be done in the future, sorry for your lost...");
			return 0;
		// Landing request, future issue
		default:
			return 0;
		}
	}

}