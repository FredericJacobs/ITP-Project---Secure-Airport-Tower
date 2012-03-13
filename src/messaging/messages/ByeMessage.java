package messaging.messages;

public class ByeMessage extends Message {
	
	public ByeMessage (byte[] planeID, int length, int priority, int posx,
			int posy) {
		super (planeID, length, priority, posx, posy, MessageType.BYE );

	}
}
