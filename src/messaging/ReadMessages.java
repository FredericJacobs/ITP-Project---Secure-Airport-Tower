package messaging;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import messaging.messages.*;

public class ReadMessages {
	public static Message readMessage(DataInputStream message)
			throws IOException {
		int posX = message.readInt();
		int posY = message.readInt();
		int messageType = message.readInt();
		byte planeID[] = new byte[16];
		int i = message.read(planeID);
		// Plane ID
		// Find the type
		System.out.println("planeID-Bytes : " + i);
		System.out.println("messageType: " + messageType);

		switch (messageType) {
		case 0:
			System.out.println("message created");
			return new HelloMessage(planeID, posX, posY, (byte) 0);
		case 6:
			return new ByeMessage(planeID, posX, posY, (byte) 0);
		default:
			return new ByeMessage(planeID, posX, posY, (byte) 0);
		//	System.out.println("message not created");
		//	return null;

		}
	}

}
