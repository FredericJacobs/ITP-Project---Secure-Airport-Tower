package messaging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import messaging.messages.HelloMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.Message;
import messaging.messages.SendRSAMessage;
import encryption.KeyPair;
import encryption.RsaOutputStream;

/**
 * This class help the Tour to handle different type of messages
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class TowerMessageHandler {
	private boolean shouldUseEncryption;
	
	public TowerMessageHandler() {
	
		shouldUseEncryption = false;
		
	}

	
	public boolean getEncryptionRouting () {
		
		return shouldUseEncryption;
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
	public boolean respond(Plane plane, Message message, OutputStream outData)
			throws IOException {
		int type = message.getType();
		switch (type) {// depends on different type of message we go to different cases 
		case 0:
			System.out.println("respond hello");
			if (((HelloMessage) message).isCrypted()) {// To see if the hello is crypted or not, then give different respond hello message
				plane.setPlaneID(message.getPlaneID());
				new HelloMessage("Tour0000".getBytes(), 0, 0, (byte) 1).write(outData);
				return true;
			}

			else {
				HelloMessage respondHelloMessage = new HelloMessage(
						"Tour0000".getBytes(), 0, 0, (byte) 0);
				plane.setPlaneID(message.getPlaneID());
				respondHelloMessage.write(outData);
				return false;
			}
			

		case 1:
			return false;

		// Data, save the file that received TDB
		case 2:
			return false;// Mayday, future issue
		case 3:// SendRSA, unfinished for the keypair

			KeyPair tourPublicKey = Tour.getDecryptKeypair();
			tourPublicKey.hidePrivateKey();
			SendRSAMessage respondRSAMessage = new SendRSAMessage(
					"Tour0000".getBytes(), 0, 0, 0, tourPublicKey);
			respondRSAMessage.write(outData);
			return false;
		// case 4,5,7 shouldnt happen to the tour
		case 6:
			System.out.println("Connection terminated");
			return false;
		case 8:
			plane.setPosx(((KeepAliveMessage) message).keepaliveX());
			plane.setPosy(((KeepAliveMessage) message).keepaliveY());
			return false;
		// keep alive
		case 9:
			System.out
					.println("To be done in the future, sorry for your lost...");
			return false;
		// Landing request, future issue
		default:
			return false;
		}
	}

}
