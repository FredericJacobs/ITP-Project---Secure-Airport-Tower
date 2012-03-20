package messaging.messages;
class RoutingMessage extends Message {
enum routingMessageType {
	NEWFIRST, LAST, REPLACEALL
}

enum moveType {
	STRAIGHT, CIRCULAR, LANDING, NONE, DESTRUCTION
}

routingMessageType TypeR;
moveType TypeM;
byte[] payload;

public RoutingMessage(byte[] planeID, int length, int posx, int posy,
		routingMessageType typeR, moveType typeM, byte[] payload) {
	super(planeID, length, 2, posx, posy, MessageType.ROUTING);
	TypeR = typeR;
	TypeM = typeM;
	this.payload = payload;
	
	}

}