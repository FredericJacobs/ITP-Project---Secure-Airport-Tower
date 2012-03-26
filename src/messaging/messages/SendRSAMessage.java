package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import encryption.*;

public class SendRSAMessage extends Message
{
	private KeyPair publicKey;
	
	public SendRSAMessage(byte[] planeID, int length, int posx,
			int posy, KeyPair myKey) {
		super (planeID, length, 2, posx, posy, MessageType.SENDRSA);
		
		myKey.hidePrivateKey();
		
		publicKey = myKey;

	}

	public KeyPair getPublicKey (){
		return publicKey;
	}
	public void write(DataOutputStream out) throws IOException{//how do we send an object? "KeyPair?"
		super.write(out);
	}
}