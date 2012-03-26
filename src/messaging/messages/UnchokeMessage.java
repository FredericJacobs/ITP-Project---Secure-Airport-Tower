package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class UnchokeMessage extends Message {
	public UnchokeMessage(byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 4, posx, posy, MessageType.UNCHOKE);
	}
	public void write(DataOutputStream out) throws IOException{
		super.write(out);
	}
}