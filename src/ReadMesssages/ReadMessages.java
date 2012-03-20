package ReadMesssages;

import java.io.IOException;
import java.io.InputStream;
import messaging.messages.*;
public class ReadMessages {
	public static Message readMessage(InputStream message) {

		byte messageBytes[] = new byte[29];

		try {
			int i = message.read(messageBytes);
			System.out.println("MessageReader-Bytes : " + i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Trouver le type de message, dans le byte 26
		int messageType = messageBytes[24] + messageBytes[25]
				+ messageBytes[26] + messageBytes[27];

		// L'ID de l'avion est dans les bytes 0 - 7
		byte planeID[] = { messageBytes[0], messageBytes[1], messageBytes[2],
				messageBytes[3], messageBytes[4], messageBytes[5],
				messageBytes[6], messageBytes[7] };

		int posX = messageBytes[16] + messageBytes[17] + messageBytes[18]
				+ messageBytes[19];
		int posY = messageBytes[20] + messageBytes[21] + messageBytes[22]
				+ messageBytes[23];

		switch (messageType) {
		case 0:
			return new HelloMessage(planeID, posX, posY, messageBytes[28], (byte)0);
		}

		return null;

	}

}
