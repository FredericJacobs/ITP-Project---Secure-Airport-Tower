package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;

public class MayDayMessage extends Message implements VisitorMessage{
	private String cause;

	public MayDayMessage(byte[] planeID, int length, int posx, int posy,
			String cause) {
		super(planeID, cause.length(), 0, posx, posy, MessageType.MAYDAY);
		this.cause = cause;
	}

	public String getCause() {
		return cause;
	}
	/** Getter to see if the hello message requires encrypted communication or not
	 * @return boolean: true for encrypted and false for not encrypted
	**/
	@Override
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
		out.write(cause.getBytes());
	}

	@Override
	public int accept(Visitor visitor,Plane plane,DataOutputStream outData){
		 return visitor.visit(plane,this,outData);						
	}

}