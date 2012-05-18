package messaging.messages;

import messaging.Visitor;

public class ChokeMessage extends Message implements VisitorMessage {
	public ChokeMessage(byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 1, posx, posy, MessageType.CHOKE);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);		
	}
}
