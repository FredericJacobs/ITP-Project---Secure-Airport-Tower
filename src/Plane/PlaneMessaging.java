package Plane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import encryption.RsaInputStream;
import encryption.RsaOutputStream;
import messaging.Plane;
import messaging.ReadMessages;
import messaging.Tower;
import messaging.TowerMessageHandler;
import messaging.messages.ByeMessage;
import messaging.messages.HelloMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.Message;
import messaging.messages.SendRSAMessage;


public class PlaneMessaging implements Runnable {
	private PlaneMessageHandler messageHandler = new PlaneMessageHandler(); // create a TowerMessageHandler to respond the messages send by the planes
	private Message mes = null;
	private DataInputStream in;
	private DataOutputStream out;
	
	@Override		
	public void run() {	

		in = TestPlane.getIn();
		out = TestPlane.getOut();
		HelloMessage HelloMessage = new HelloMessage(TestPlane.getPlaneID(), 0, 0, TestPlane.isEncryptionEnabledAtLaunch() ? (byte) (1 << 4):(byte) 0);
		try {
			HelloMessage.write(out);
		} catch (IOException e1) {
			
		}
		
		while (true){
			try {
				mes = ReadMessages.readMessage(in);
				TestPlane.addMessageToIncomingQueue(mes);
				messageHandler.respond(TestPlane.getNextMessageIncomingQueue(), out);
			} catch (IOException e) {
				e.printStackTrace();
				}
		}
	}
}