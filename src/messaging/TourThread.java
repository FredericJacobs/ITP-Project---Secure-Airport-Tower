package messaging;

import java.net.*;
import java.io.*;

import messaging.messages.ByeMessage;
import messaging.messages.HelloMessage;
import messaging.messages.KeepAliveMessage;
import messaging.messages.Message;
import messaging.messages.SendRSAMessage;
import encryption.*;

public class TourThread extends Thread {

	private Socket socket = null;

	public TourThread(Socket socket) {
		super("TourMultiServerThread");
		this.socket = socket;
	}

	public void run() {

		try {
			
			
			DataOutputStream outData = new DataOutputStream(
					socket.getOutputStream());
			DataInputStream inData = new DataInputStream(
					socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			while (true) {
				Message mes = ReadMessages.readMessage(inData);
				if (mes.getType() != 6) {
					mes.print();
					respond(mes, outData);
				} else {
					mes.print();
					respond(mes, outData);
					System.out.println("Bye! Bon voyage");
					break;
				}
			}
			// finish the network and close the tunnel
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Message respond(Message message, DataOutputStream outData)
			throws IOException {
		int type = message.getType();
		switch (type) {
		case 0:
			System.out.println("respond hello");
			
			
			
			if (((HelloMessage) message).isCrypted()) { 
				
				KeyPair tourPublicKey = Tour.getDecryptKeypair();
				tourPublicKey.hidePrivateKey();
				SendRSAMessage respondHelloMessage = new SendRSAMessage ("Tour0000".getBytes(), 0, 0, 0, tourPublicKey);
				respondHelloMessage.write(outData);
				return respondHelloMessage;
			}
			
			else {
				
				HelloMessage respondHelloMessage = new HelloMessage("Tour0000".getBytes(), 0, 0, (byte) 0);
				respondHelloMessage.write(outData);
				return respondHelloMessage;
					 
			}
		
			
		case 1:
			// Data, save the file that received TDB
		case 2:// Mayday, future issue
		case 3:// SendRSA, unfinished for the keypair
			KeyPair tourPublicKey = Tour.getDecryptKeypair();
			tourPublicKey.hidePrivateKey();
			
			return new SendRSAMessage ("Tour0000".getBytes(), 0, 0, 0, tourPublicKey);
			
			// case 4,5,7 shouldnt happen to the tour
		case 6:
			System.out.println("Connection terminated");
			return null;
		case 8:
			Tour.setkeepaliveX(((KeepAliveMessage) message).keepaliveX());
			Tour.setkeepaliveY(((KeepAliveMessage) message).keepaliveY());
			System.out.println("keepaliveX :" + ((KeepAliveMessage) message).keepaliveX());
			System.out.println("keepaliveY :" + ((KeepAliveMessage) message).keepaliveY());
			return null;
			// keep alive
		case 9: 
			System.out.println("To be done in the future, sorry for your lost...");
			return null;
			// Landing request, future issue
		default:
			return null;
		}
	}
}
