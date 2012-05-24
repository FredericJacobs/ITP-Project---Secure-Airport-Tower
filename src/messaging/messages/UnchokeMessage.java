package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;

public class UnchokeMessage extends Message implements VisitorMessage {
	public UnchokeMessage(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 4, posx, posy, MessageType.UNCHOKE);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
	}

	@Override
	public int accept(Visitor visitor,Plane plane,DataOutputStream outData){
		 return visitor.visit(plane,this,outData);						
	}
}