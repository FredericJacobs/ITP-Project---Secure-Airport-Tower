package messaging.messages;

public class KeepAliveMessage extends Message {
	public KeepAliveMessage (byte[] planeID, int length, int priority, int posx,
			int posy) {
		super (planeID, length, priority, posx, posy, MessageType.KEEPALIVE);
	}
}
