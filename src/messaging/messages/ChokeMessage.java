package messaging.messages;

public class ChokeMessage extends Message {
	public ChokeMessage(byte[] planeID, int length, int priority, int posx,
			int posy) {
		super (planeID, length, priority, posx, posy, MessageType.CHOKE);
	}
}
