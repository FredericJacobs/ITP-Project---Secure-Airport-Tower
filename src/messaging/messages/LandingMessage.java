package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;

public class LandingMessage extends Message implements VisitorMessage {
	public LandingMessage(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 2, posx, posy, MessageType.LANDINGREQUEST);
	}

	public void write(DataOutputStream out) throws IOException {
		super.write(out);
	}

	@Override
	public int accept(Visitor visitor,Plane plane,DataOutputStream outData){
		 return visitor.visit(plane,this,outData);						
	}
}
