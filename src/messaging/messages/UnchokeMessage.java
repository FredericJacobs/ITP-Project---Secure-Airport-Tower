package messaging.messages;

public class UnchokeMessage extends Message {
	public UnchokeMessage(byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 4, posx, posy, MessageType.UNCHOKE);
	}
}