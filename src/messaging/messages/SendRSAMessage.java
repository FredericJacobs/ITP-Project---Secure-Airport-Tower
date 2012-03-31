package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import encryption.*;

public class SendRSAMessage extends Message {
	private KeyPair publicKey;

	public SendRSAMessage(byte[] planeID, int length, int posx, int posy,
			KeyPair myKey) {
		super(planeID, length, 2, posx, posy, MessageType.SENDRSA);
		publicKey = myKey;
	}
	/**
	 ** Getter of the publickey
	 **/
	public KeyPair getPublicKey() {
		return publicKey;
	}
	/**
	 ** Override of the write message, to send out the supplementary information
	 **/
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
		out.writeInt(publicKey.getKeySize());
		out.writeInt(publicKey.getModulus().length);
		out.write(publicKey.getModulus());
		out.write(publicKey.getPublicKey());
	}
}