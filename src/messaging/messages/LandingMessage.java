package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class LandingMessage extends Message {
	public LandingMessage (byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 2, posx, posy, MessageType.LANDINGREQUEST);
	}
	public void write(DataOutputStream out) throws IOException{
		super.write(out);
	}
}
