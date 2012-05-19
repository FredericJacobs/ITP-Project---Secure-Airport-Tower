package messaging.messages;

import java.io.DataOutputStream;

import messaging.Plane;
import messaging.Visitor;

public class ChokeMessage extends Message implements VisitorMessage {
	public ChokeMessage(byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 1, posx, posy, MessageType.CHOKE);
	}

	@Override
	public int accept(Visitor visitor,Plane plane,DataOutputStream outData){
		 return visitor.visit(plane,this,outData);						
	}
}
