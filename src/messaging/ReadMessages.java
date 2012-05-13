package messaging;

import java.io.DataInputStream;
import java.io.IOException;
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
	 * @param message  The coming DataInputStream
	 * @return Message The reformed new created Message
	 * @throws IOException
	 */

	public static Message readMessage(DataInputStream message){

		/**
		 * to read the DataInputStream in the same order with the
		 * DataOutputStream
		 */

		byte planeID[] = new byte[8];
		try {
		message.read(planeID);
		int length = message.readInt();
		@SuppressWarnings("unused")
		int priority = message.readInt();
		int posX = message.readInt();
		int posY = message.readInt();
		int messageType = message.readInt();
		// Plane ID
		/**
		 * After all the basic parameters of the Message have been saved, we
		 * return the different type of the DataOutputStream, by judging the messageType
		 */
		switch (messageType) {
		case 0: // HelloMessage
			byte reserved = message.readByte();
			return new HelloMessage(planeID, posX, posY, reserved);
		case 1://DataMessage
			byte[] hash = new byte[20];
			byte[] format = new byte[4]; // 4 octets (4 bytes)
			byte[] payload = new byte[length];
			message.read(hash);
			int continuation = message.readInt();
			message.read(format);
			int fileSize = message.readInt();
			message.read(payload);
			return new DataMessage(planeID, continuation, posX, posY, hash,
					format, fileSize, payload);
		case 2://MayDayMessage
			
			System.out.println("Mayday message recieved!");
			String cause = message.readUTF();
			return new MayDayMessage(planeID, cause.length(), posX, posY, cause);
		case 3://SendRSAMessage

			int keySize = message.readInt();
			int modulusLength = message.readInt();
			byte[] modulus = new byte[modulusLength];
			message.read(modulus);
			int publicKeyLength = message.readInt();
			byte[] publicKey = new byte[publicKeyLength];
			message.read(publicKey);
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
			int TypeR = message.readInt();
			int TypeM = message.readInt();
			byte[] payloadOfRouting = new byte[length];
			message.read(payloadOfRouting);
			return new RoutingMessage(planeID, posX,
					posY, routingMessageType.values()[TypeR],
					moveType.values()[TypeM], payloadOfRouting);
		case 8://KeepAliveMessage
			return new KeepAliveMessage(planeID, posX, posY);
		case 9://LandingMessage
			return new LandingMessage(planeID, 0, posX, posY);
		default://ByeMessage
			System.out.println("message not created");
			return null;// If the messagetype doesnt match then we break the link by sending a Bye
		}} catch (IOException e) {
			e.printStackTrace();
			return null;// If the messagetype doesnt match then we break the link by sending a Bye;

		}
	}
}