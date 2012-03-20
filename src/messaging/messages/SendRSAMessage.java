package messaging.messages;

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
}