package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class KeepAliveMessage extends Message {
	public KeepAliveMessage(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 3, posx, posy, MessageType.KEEPALIVE);
	}

	public void write(DataOutputStream out) throws IOException {
		super.write(out);
	}

	public int keepaliveX() {
		return posx;
	}

	public int keepaliveY() {
		return posy;
	}
}
