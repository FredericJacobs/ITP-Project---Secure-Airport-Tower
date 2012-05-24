package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;

public class RoutingMessage extends Message implements VisitorMessage {
	public enum routingMessageType {
		NEWFIRST, LAST, REPLACEALL;
	}

	public enum moveType {
		STRAIGHT, CIRCULAR, LANDING, NONE, DESTRUCTION;

	}

	private routingMessageType TypeR;

	public routingMessageType getTypeR() {
		return TypeR;
	}

	public moveType getTypeM() {
		return TypeM;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	private moveType TypeM;
	private byte[] payload;

	public RoutingMessage(byte[] planeID, int posx, int posy,
			routingMessageType typeR, moveType typeM, byte[] payload) {
		super(planeID, payload.length, 2, posx, posy, MessageType.ROUTING);
		TypeR = typeR;
		TypeM = typeM;
		this.payload = payload;
	}

	/**
	 ** Override of the write message, to send out the supplementary information
	 **/
	@Override
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
		out.writeInt(TypeR.ordinal());
		out.writeInt(TypeM.ordinal());
		out.write(payload);
	}

	@Override
	public int accept(Visitor visitor, Plane plane, DataOutputStream outData) {
		return visitor.visit(plane, this, outData);
	}

}