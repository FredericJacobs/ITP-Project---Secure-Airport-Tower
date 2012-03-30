package messaging;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.messages.HelloMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.Message;
import messaging.messages.SendRSAMessage;
import encryption.KeyPair;

public class TowerMessageHandler {
	public TowerMessageHandler() {
	}

	public void respond(Plane plane, Message message, DataOutputStream outData)
			throws IOException {
		int type = message.getType();
		switch (type) {
		case 0:
			System.out.println("respond hello");

			if (((HelloMessage) message).isCrypted()) {
				// KeyPair tourPublicKey = Tour.getDecryptKeypair();
				// tourPublicKey.hidePrivateKey();
				// SendRSAMessage respondHelloMessage = new SendRSAMessage(
				// "Tour0000".getBytes(), 0, 0, 0, tourPublicKey);
				// respondHelloMessage.write(outData);
				plane.setPlaneID(message.getPlaneID());
				new HelloMessage("Tour0000".getBytes(), 0, 0, (byte) 1)
						.write(outData);
			}

			else {
				HelloMessage respondHelloMessage = new HelloMessage(
						"Tour0000".getBytes(), 0, 0, (byte) 0);
				plane.setPlaneID(message.getPlaneID());
				respondHelloMessage.write(outData);
			}
			break;

		case 1:
			break;

		// Data, save the file that received TDB
		case 2:
			break;// Mayday, future issue
		case 3:// SendRSA, unfinished for the keypair

			KeyPair tourPublicKey = Tour.getDecryptKeypair();
			tourPublicKey.hidePrivateKey();
			SendRSAMessage respondRSAMessage = new SendRSAMessage(
					"Tour0000".getBytes(), 0, 0, 0, tourPublicKey);
			respondRSAMessage.write(outData);
			break;
		// case 4,5,7 shouldnt happen to the tour
		case 6:
			System.out.println("Connection terminated");
			break;
		case 8:
			Tour.setkeepaliveX(((KeepAliveMessage) message).keepaliveX());
			Tour.setkeepaliveY(((KeepAliveMessage) message).keepaliveY());
			System.out.println("keepaliveX :"
					+ ((KeepAliveMessage) message).keepaliveX());
			System.out.println("keepaliveY :"
					+ ((KeepAliveMessage) message).keepaliveY());
			break;
		// keep alive
		case 9:
			System.out
					.println("To be done in the future, sorry for your lost...");
			break;
		// Landing request, future issue
		default:
			break;
		}
	}

}
