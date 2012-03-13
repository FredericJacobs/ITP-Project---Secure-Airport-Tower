package messaging.messages;

public class LandingMessage extends Message {
	public LandingMessage (byte[] planeID, int length, int priority, int posx,
			int posy) {
		super (planeID, length, priority, posx, posy, MessageType.LANDINGREQUEST);
	}
}
