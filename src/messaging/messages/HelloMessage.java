package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class HelloMessage extends Message {
	private byte reserved;
	public HelloMessage (byte[] planeID, int length, int posx,
			int posy, byte reserved) {
		super (planeID, length, 1, posx, posy, MessageType.HELLO);
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
		out.write(planeID);
		out.writeInt(length);
		out.write(1);
		out.write(posx);
		out.write(posy);		
		out.write(MessageType.HELLO.ordinal());
	}
	public byte[] getByte(){ //Save the information of Hell in to an array of byte
		byte [] Byte  = new byte[100];
		Byte[0] = (byte)1;
		Byte[1] = (byte)length;		
		Byte[2] = (byte)posx;		
		Byte[3] = (byte)posy;
		Byte[4] = reserved;
		int n = 0;
		for (int i = 5; i<planeID.length;i++){
			Byte [i] = planeID[n];
			n++;
		}
		return Byte;
	}
}
