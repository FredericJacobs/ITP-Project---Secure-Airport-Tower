package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class KeepAliveMessage extends Message {
	public KeepAliveMessage(byte[] planeID, int posx, int posy) {
		super(planeID, 0, 3, posx, posy, MessageType.KEEPALIVE);
	}

	public void write(DataOutputStream out) throws IOException {
		super.write(out);
	}

	/**
	 * Getter of the posX, posY
	 * 
	 * @return int x and y
	 **/
	public int keepaliveX() {
		return posx;
	}

	public int keepaliveY() {
		return posy;
	}
}

