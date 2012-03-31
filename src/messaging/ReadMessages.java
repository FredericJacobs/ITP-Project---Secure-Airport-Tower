package messaging;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.*;
import encryption.KeyPair;
import messaging.messages.*;
import messaging.messages.RoutingMessage.moveType;
import messaging.messages.RoutingMessage.routingMessageType;

/**
 * This class aims at converting the DataInputStream into a new created message,
 * by using the method read (for byte[]) and readInt.
 ** 
 ** @param The DataInputStream by which we communicated.
 ** @author Hantao Zhao
 ** @author Frederic Jacobs
 */
public class ReadMessages {
	/**
	 * This method will convert the DataInputStream into a new created message,
	 * by using the method read (for byte[]) and readInt.
	 * 
	 * @param inData  The coming DataInputStream
	 * @return Message The reformed new created Message
	 * @throws IOException
	 */
	public static Message readMessage(InputStream inData)
			throws IOException {

		/**
		 * to read the DataInputStream in the same order with the
		 * DataOutputStream
		 */
		byte planeID[] = new byte[8];
		int i = inData.read(planeID);
		int length = inData.read();
		int priority = inData.read();
		int posX = inData.read();
		int posY = inData.read();

		int messageType = inData.read();
		// Plane ID
		/**
		 * After all the basic parameters of the Message have been saved, we
		 * return the different type of the DataOutputStream, by judging the messageType
		 */
		switch (messageType) {
		case 0: // HelloMessage
			byte reserved = (byte) inData.read();
			return new HelloMessage(planeID, posX, posY, reserved);
		case 1://DataMessage
			byte[] hash = new byte[20];
			byte[] format = new byte[4]; // 4 octets (4 bytes)
			byte[] payload = new byte[4];
			inData.read(hash);
			int continuation = inData.read();
			inData.read(format);
			int fileSize = inData.read();
			inData.read(payload);
			return new DataMessage(planeID, continuation, posX, posY, hash,
					format, fileSize, payload);
		case 2://MayDayMessage
			int lengthcause = inData.read();
			byte[] cause = new byte[lengthcause];
			String str = new String(cause);
			return new MayDayMessage(planeID, cause.length, posX, posY, str);
		case 3://SendRSAMessage
			int keySize = inData.read();
			int modulusLength = inData.read();
			byte[] modulus = new byte[modulusLength];
			byte[] publicKey = new byte[keySize];
			inData.read(modulus);
			inData.read(publicKey);
			return new SendRSAMessage(planeID, 0, posX, posY, new KeyPair(
					new BigInteger(modulus), new BigInteger(publicKey), null,
					keySize));
		case 4://ChokeMessage
			return new ChokeMessage(planeID, 0, posX, posY);
		case 5://UnchokeMessage
			return new UnchokeMessage(planeID, 0, posX, posY);
		case 6://ByeMessage
			return new ByeMessage(planeID, 0, posX, posY);
		case 7://RoutingMessage
			int TypeR = inData.read();
			int TypeM = inData.read();
			byte[] payloadOfRouting = new byte[20];
			inData.read(payloadOfRouting);
			return new RoutingMessage(planeID, payloadOfRouting.length, posX,
					posY, routingMessageType.routingMessageTypeName(TypeR),
					moveType.moveMessageTypeName(TypeM), payloadOfRouting);
		case 8://KeepAliveMessage
			System.out.println("Keep alive X = " + posX);
			System.out.println("Keep alive Y = " + posY);
			return new KeepAliveMessage(planeID, posX, posY);
		case 9://LandingMessage
			return new LandingMessage(planeID, 0, posX, posY);
		default://ByeMessage
			System.out.println("message not created");
			return new ByeMessage("Bye".getBytes(), 0, posX, posY);// If the messagetype doesnt match then we break the link by sending a Bye
		}
	}
}
