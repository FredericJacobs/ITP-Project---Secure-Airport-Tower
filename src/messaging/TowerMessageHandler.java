package messaging;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.messages.HelloMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.Message;
import messaging.messages.SendRSAMessage;
import encryption.KeyPair;

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
	 * @param message
	 *            The message that need to be handled
	 * @param outData
	 *            The DataOutputStream where we send the feed back message
	 * @throws IOException
	 */
	public void respond(Plane plane, Message message, DataOutputStream outData)
			throws IOException {
		int type = message.getType();
		switch (type) {// depends on different type of message we go to different cases 
		case 0:
			System.out.println("respond hello");
			if (((HelloMessage) message).isCrypted()) {// To see if the hello is crypted or not, then give different respond hello message

				plane.setPlaneID(message.getPlaneID());
				new HelloMessage("Tour0000".getBytes(), 0, 0, (byte) 1).write(outData);
			}

			else {
				HelloMessage respondHelloMessage = new HelloMessage(
						"Tour0000".getBytes(), 0, 0, (byte) 0);
				plane.setPlaneID(message.getPlaneID());
				respondHelloMessage.write(outData);
			}
			break;

		case 1:
			break;

		// Data, save the file that received TDB
		case 2:
			break;// Mayday, future issue
		case 3:// SendRSA, unfinished for the keypair

			KeyPair tourPublicKey = Tour.getDecryptKeypair();
			tourPublicKey.hidePrivateKey();
			SendRSAMessage respondRSAMessage = new SendRSAMessage(
					"Tour0000".getBytes(), 0, 0, 0, tourPublicKey);
			respondRSAMessage.write(outData);
			break;
		// case 4,5,7 shouldnt happen to the tour
		case 6:
			System.out.println("Connection terminated");
			break;
		case 8:
			plane.setPosx(((KeepAliveMessage) message).keepaliveX());
			plane.setPosy(((KeepAliveMessage) message).keepaliveY());
			break;
		// keep alive
		case 9:
			System.out
					.println("To be done in the future, sorry for your lost...");
			break;
		// Landing request, future issue
		default:
			break;
		}
	}

}
