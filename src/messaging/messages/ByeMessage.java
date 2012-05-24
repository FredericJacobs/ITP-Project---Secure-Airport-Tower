package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;

public class ByeMessage extends Message implements VisitorMessage {

	public ByeMessage(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 3, posx, posy, MessageType.BYE);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
	}

	@Override
	public int accept(Visitor visitor, Plane plane, DataOutputStream outData) {
		return visitor.visit(plane, this, outData);
	}
}
