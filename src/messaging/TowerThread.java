package messaging;

import java.net.*;
import java.util.Observable;
import java.util.Observer;
import java.io.*;

import messaging.messages.*;
import encryption.*;

/**
 * This class help the Tour to realize the Multiply client, that is to say to
 * handle different planes at the same time by extending the class Thread
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class TowerThread extends Thread implements Observer {

	private Socket socket = null;
	private int encryptionStatus;
	private Message mes;
	private DataOutputStream outData;
	private DataInputStream inData;


	public TowerThread(Socket socket) {
		super("TourMultiServerThread");
		this.socket = socket;
		 
	}

	/**
	 * Override of the method run(). All the functions of the tour should be
	 * added in here
	 */
	public void run() {

		try {
			outData = new DataOutputStream(
					socket.getOutputStream());
			inData = new DataInputStream(
					socket.getInputStream());

			TowerMessageHandler messageHandler = new TowerMessageHandler(); // create a TowerMessageHandler to respond the messages send by the planes
			int planenumber = Tower.planeCounter;// To get a number of the planes which connect with the tour.
			Tower.planeCounter++; //  The planeCounter ++,  for the next plane
			Tower.planes[planenumber] = new Plane(); // Created a new plane by using the order
			Tower.planes[planenumber].setSocket(this.socket);
			while (true) {
				mes = ReadMessages.readMessage(inData);
				// read the message send by the DataInputStream
				Tower.addMessageToIncomingQueue(mes);// Add it into the incomingQueue				
			
				if (mes.getType() != 6) {            // Handle the message , if the messageType isnt Bye, then go to the next
					
					encryptionStatus = (messageHandler.respond(Tower.planes[planenumber], planenumber , Tower.getNextMessageIncomingQueue(), outData));
					switch (encryptionStatus){	
					case 0: break; 
					case 1: inData = new DataInputStream( new RsaInputStream(socket.getInputStream(), Tower.getDecryptKeypair()));System.out.println("DECRYPTING"); break;
					case 2: outData = new DataOutputStream(new RsaOutputStream(socket.getOutputStream(), Tower.planes[planenumber].getKeypair())); System.out.println("ENCRYPTING"); break;
					}
					
				} else {
					// Handle the bye message and stop reading from the plane
					
					messageHandler.respond(Tower.planes[planenumber],
							planenumber, Tower.getNextMessageIncomingQueue(), outData);
					System.out.println("Bye! Bon voyage");
					break;
				}
			}
			// finish the network and close the tunnel
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Connection interrupted");
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		
		System.out.println("Begin Chock mode");
		ChokeMessage respondHelloMessage = new ChokeMessage(
				"Tour0000".getBytes(), 0, 0, 0);
		Event eventR = new Event(respondHelloMessage, "Tower",
				"AllPlanes");
		Tower.journal.addEvent(eventR);
		try {
			respondHelloMessage.write(outData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
	}
}