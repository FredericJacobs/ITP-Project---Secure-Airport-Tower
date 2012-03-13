package messaging.messages;

public class KeepAliveMessage extends Message {
	public KeepAliveMessage (byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 3, posx, posy, MessageType.KEEPALIVE);
	}
}
