package messaging;

import java.net.*;
import java.io.*;

import messaging.Tour;
import messaging.messages.*;
import encryption.*;

/**
 * This class help the Tour to realize the Multiply client, that is to say to
 * handle different planes at the same time by extending the class Thread
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class TourThread extends Thread {

	private Socket socket = null;

	public TourThread(Socket socket) {
		super("TourMultiServerThread");
		this.socket = socket;
	}

	/**
	 * Override of the method run(). All the functions of the tour should be
	 * added in here
	 */
	public void run() {

		try {
			
			OutputStream outData = new DataOutputStream(
					socket.getOutputStream());
			InputStream inData = new DataInputStream(
					socket.getInputStream());

			TowerMessageHandler messageHandler = new TowerMessageHandler(); // create a TowerMessageHandler to respond the messages send by the planes
			int planenumber = Tour.planeCounter; // To get a number of the planes which connect with the tour.
			Tour.planeCounter ++; //  The planeCounter ++,  for the next plane
			Tour.planes[planenumber] = new Plane(); // Created a new plane by using the order
			while (true) {
				
				Message mes = ReadMessages.readMessage(inData);				// read the message send by the DataInputStream

				Tour.addMessageToIncomingQueue(mes);				// Add it into the incomingQueue
				
				
				try {				//Give the tour a moment to respond
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				
				if (mes.getType() != 6) {            // Handle the message , if the messageType isnt Bye, then go to the next
					if (messageHandler.respond(Tour.planes[planenumber], Tour.getNextMessageIncomingQueue(), outData)){
					outData = new RsaOutputStream (socket.getOutputStream(), Tour.getDecryptKeypair() );
					inData = new RsaInputStream(socket.getInputStream(), Tour.planes[planenumber].getKeypair());
					}
					
					
				} else {							// Handle the bye message and stop reading from the plane
					messageHandler.respond(Tour.planes[planenumber],
							Tour.getNextMessageIncomingQueue(), outData);
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
}