package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class HelloMessage extends Message {
	private byte reserved;
	public HelloMessage (byte[] planeID, int posx,
			int posy, byte reserved) {
		super (planeID, 0, 1, posx, posy, MessageType.HELLO);
		this.reserved = reserved;
	}
	public boolean isCrypted (){
		boolean flag = false;
		if ((int)reserved == 1){
			flag = true;
		}
		return flag;
	}
	public void write(DataOutputStream out) throws IOException{
		super.write(out);
		out.write(reserved);
	}
}
