package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;

public class HelloMessage extends Message implements VisitorMessage {
	private byte reserved;

	public HelloMessage(byte[] planeID, int posx, int posy, byte reserved) {
		super(planeID, 0, 1, posx, posy, MessageType.HELLO);
		this.reserved = reserved;
	}

	/**
	 * Getter to see if the hello message requires Crypted communication or not
	 * 
	 * @return boolean: true for Crypted and false for not Crypted
	 **/
	public boolean isCrypted() {
		boolean flag = false;
		if (reserved == 1 << 4) {
			flag = true;
		}
		return flag;
	}

	/**
	 * An override of the write message , to send out one byte to represent if
	 * the Hello message is crypted
	 * 
	 * @return boolean: true for Crypted and false for not Crypted
	 **/
	@Override
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
		out.write(reserved);
	}

	@Override
	public int accept(Visitor visitor, Plane plane, DataOutputStream outData) {
		return visitor.visit(plane, this, outData);
	}
}
