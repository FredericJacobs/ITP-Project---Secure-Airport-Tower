package messaging.messages;

public class LandingMessage extends Message {
	public LandingMessage (byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 2, posx, posy, MessageType.LANDINGREQUEST);
	}
}
