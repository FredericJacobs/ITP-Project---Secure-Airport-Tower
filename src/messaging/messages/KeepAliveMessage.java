package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;

public class KeepAliveMessage extends Message implements VisitorMessage{
	public KeepAliveMessage(byte[] planeID, int posx, int posy) {
		super(planeID, 0, 3, posx, posy, MessageType.KEEPALIVE);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
	}

	/**
	 * Getter of the posX, posY
	 * 
	 * @return int x and y
	 **/
	public int keepaliveX() {
		return posx;
	}

	public int keepaliveY() {
		return posy;
	}

	@Override
	public int accept(Visitor visitor,Plane plane,DataOutputStream outData){
		 return visitor.visit(plane,this,outData);						
	}
}

