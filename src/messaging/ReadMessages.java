package messaging;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.*;
import encryption.KeyPair;
import messaging.messages.*;
import messaging.messages.RoutingMessage.moveType;
import messaging.messages.RoutingMessage.routingMessageType;
// This method aims at converting the DataInputStream into a new created message, by using the method read (for byte[]) and readInt
//Right now(26.03) it can transfer all kinds of message except the sendRSA because I cant find a way to transfer the data keypair

public class ReadMessages {
	public static Message readMessage(DataInputStream message)
			throws IOException {
		byte planeID[] = new byte[8];

		int i = message.read(planeID);
		int posX = message.readInt();
		int posY = message.readInt();
		int messageType = message.readInt();
		// Plane ID
		// Find the type
		System.out.println("planeID-Bytes : " + i);
		System.out.println("messageType: " + messageType);

		switch (messageType) {
		case 0:
			byte reserved = message.readByte();
			return new HelloMessage(planeID, posX, posY, reserved);
		case 1:
			byte[] hash = new byte[20];
			byte[] format = new byte[4]; // 4 octets (4 bytes)
			byte[] payload = new byte[4];
			message.read(hash);
			int continuation = message.readInt();
			message.read(format);
			int fileSize = message.readInt();
			message.read(payload);
			return new DataMessage(planeID, continuation, posX, posY, hash,
					format, fileSize, payload);
		case 2:
			int length = message.readInt();
			byte[] cause = new byte[length]; // we assume that all the cause can
												// be
												// stated in 20 letters
			String str = new String(cause);
			return new MayDayMessage(planeID, cause.length, posX, posY, str);
		case 3:
			int keySize = message.readInt();
			int modulusLength = message.readInt();
			byte[] modulus = new byte[modulusLength];
			byte[] publicKey = new byte[keySize];
			message.read(modulus);
			message.read(publicKey);
			return new SendRSAMessage(planeID,0,posX, posY, new KeyPair (new BigInteger(modulus),new BigInteger(publicKey) , null ,keySize));
		case 4:
			return new ChokeMessage(planeID, 0, posX, posY);
		case 5:
			return new UnchokeMessage(planeID, 0, posX, posY);
		case 6:
			return new ByeMessage(planeID, 0, posX, posY);
		case 7:
			int TypeR = message.readInt();
			int TypeM = message.readInt();
			byte[] payloadOfRouting = new byte[20];
			message.read(payloadOfRouting);
			return new RoutingMessage(planeID, payloadOfRouting.length, posX,
					posY, routingMessageType.routingMessageTypeName(TypeR),
					moveType.moveMessageTypeName(TypeM), payloadOfRouting);
		case 8:
			return new KeepAliveMessage(planeID, posX, posY, (byte) 0);
		case 9:
			return new LandingMessage(planeID, posX, posY, (byte) 0);
		default:
			System.out.println("message not created");
			return new ByeMessage("Bye".getBytes(), posX, posY, (byte) 0);
		}
	}
}
