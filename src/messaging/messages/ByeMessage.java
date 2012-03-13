package messaging.messages;

public class ByeMessage extends Message {
	
	public ByeMessage (byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 4, posx, posy, MessageType.BYE );

	}
}
