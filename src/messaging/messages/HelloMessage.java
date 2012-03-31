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
	/** Getter to see if the hello message requires Crypted communication or not
	 * @return boolean: true for Crypted and false for not Crypted
	**/
	public boolean isCrypted (){
		boolean flag = false;
		if ((int)reserved == 1){
			flag = true;
		}
		return flag;
	}
	/** An override of the write message , to send out one byte to represent if the Hello message is crypted
	 * @return boolean: true for Crypted and false for not Crypted
	**/
	public void write(DataOutputStream out) throws IOException{
		super.write(out);
		out.write(reserved);
	}
}
