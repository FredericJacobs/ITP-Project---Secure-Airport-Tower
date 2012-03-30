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
			TowerMessageHandler messageHandler = new TowerMessageHandler();
			int planenumber = Tour.planeCounter;
			Tour.planeCounter++;
			while (true) {
				Message mes = ReadMessages.readMessage(inData);
				if (mes.getType() != 6) {
					mes.print();
					messageHandler.respond(Tour.plane[planenumber],mes, outData);
				} else {
					mes.print();
					messageHandler.respond(Tour.plane[planenumber],mes, outData);
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
}