package messaging.messages;

public class UnchokeMessage extends Message {
	public UnchokeMessage(byte[] planeID, int length, int priority, int posx,
			int posy) {
		super (planeID, length, priority, posx, posy, MessageType.UNCHOKE);
	}
}