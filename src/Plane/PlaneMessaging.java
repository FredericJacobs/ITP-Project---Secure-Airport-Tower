package Plane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import messaging.ReadMessages;
import messaging.messages.ByeMessage;
import messaging.messages.HelloMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.SendRSAMessage;


public class PlaneMessaging implements Runnable {

	public void run(DataInputStream in, DataOutputStream out) {
		while (true){
			
			

		case 0:
			HelloMessage hello = new HelloMessage(planeID.getBytes(), 20, 10, (byte) 0);
			hello.print();
			hello.write(out);
			System.out.println("----Messages from the tour-----");
			ReadMessages.readMessage(in).print();
			break;
		case 1: 
		case 2:
		case 3: 
			
			// Encryption Support broken in this test. Use given planes
			SendRSAMessage sendRSA = new SendRSAMessage(planeID.getBytes(),8, 20, 10,decryptKeypair);
			sendRSA.write(out);
			ReadMessages.readMessage(in).print();
			break;
			
		case 6:
			ByeMessage bye = new ByeMessage(planeID.getBytes(), 0, 20, 10);
			bye.write(out);
			System.out.println("----Messages from the tour-----");
			ReadMessages.readMessage(in).print();
			System.out.println("Bye! Bon voyage!");
			break;
		case 8:
			KeepAliveMessage KeepAlive = new KeepAliveMessage(planeID.getBytes(), plane.getPosition().getPosx(), plane.getPosition().getPosy());
			KeepAlive.write(out);
			System.out.println("----Messages from the tour-----no return message");
			break;
		
	}
	
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
