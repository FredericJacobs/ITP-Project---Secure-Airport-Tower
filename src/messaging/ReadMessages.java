package messaging;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import messaging.messages.*;
public class ReadMessages {
	public static Message readMessage(DataInputStream message) {

		byte messageBytes[] = new byte[24];

		try {
			int i = message.read(messageBytes);
			System.out.println("MessageReader-Bytes : " + i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Find the type
		int messageType = messageBytes[20] + messageBytes[21]
				+ messageBytes[22] + messageBytes[23];

		// Plane ID
		byte planeID[] = { messageBytes[0], messageBytes[1], messageBytes[2],
				messageBytes[3], messageBytes[4] };
		int posX = messageBytes[5] + messageBytes[6] + messageBytes[7]
				+ messageBytes[8];
		int posY = messageBytes[9] + messageBytes[10] + messageBytes[11]
				+ messageBytes[12];

		switch (messageType) {
		case 0:
			return new HelloMessage(planeID,0, posX, posY, (byte)0);
		}
		return null;
	}

}
