package messaging.messages;

public class ChokeMessage extends Message {
	public ChokeMessage(byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 1, posx, posy, MessageType.CHOKE);
	}
}
