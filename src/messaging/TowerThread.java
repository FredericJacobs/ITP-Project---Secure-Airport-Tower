package messaging;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import messaging.messages.Message;
import messaging.messages.SendRSAMessage;
import twitter.SendTweet;
import encryption.RsaInputStream;
import encryption.RsaOutputStream;

/**
 * This class help the Tower to handle communications with several planes, that
 * is to say that each thread handles the connection with one plane.
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class TowerThread extends Thread {

	private Socket socket = null;
	private int encryptionStatus;
	private Message mes;
	private DataOutputStream outData;
	private DataInputStream inData;
	private Plane plane;

	public TowerThread(Socket socket) {
		super("TourMultiServerThread");
		this.socket = socket;

	}

	/**
	 * Override of the method run(). All the functions of the tour should be
	 * added in here
	 */
	@Override
	public void run() {

		try {
			outData = new DataOutputStream(socket.getOutputStream());
			inData = new DataInputStream(socket.getInputStream());
			// Create a tower message handler
			TowerMessageHandler messageHandler = new TowerMessageHandler();
			// Created a new plane and add it into the arraylist
			plane = new Plane();
			Tower.getInstance().getPlanes().add(plane);
			plane.setSocket(this.socket);

			while (true) {
				mes = ReadMessages.readMessage(inData);

				// If the message is crypted then we save the public key
				if (mes.getType() == 3) {
					SendRSAMessage message = (SendRSAMessage) mes;
					plane.setKeypair(message.getPublicKey());
				}
				
				// Handle the message , if the messageType isn't Bye, then answer it by using the messageHandler's respond 
				if (mes.getType() != 6) {

					encryptionStatus = (messageHandler.respond(plane, mes,
							outData));
					switch (encryptionStatus) {
					case 0:
						break;
					case 1:
						inData = new DataInputStream(new RsaInputStream(
								socket.getInputStream(),
								Tower.getDecryptKeypair()));
						System.out.println("DECRYPTING");
						break;
					case 2:
						outData = new DataOutputStream(new RsaOutputStream(
								socket.getOutputStream(), plane.getKeypair()));
						System.out.println("ENCRYPTING");
						break;
					}

				}
				
				// Handle the bye message and stop reading from the plane
				else {
					messageHandler.respond(plane, mes, outData);
					System.out.println("Bye! Bon voyage");
					Tower.planDidLandSafely(plane.getPlaneID());
					SendTweet.publish("Plane " + plane.getPlaneID()
							+ " has LANDED at ITP Airport at "
							+ new Date().toString() + " #ICITP2012");
					break;
				}
			}
			// finish the network and close the tunnel
		} catch (IOException e) {
			try {
				// If there is a plane crushed or lose the connection, we remove it from the plane list 
				// and re-organize all the communicating planes
				System.out.println("One Plane lost,too bad");
				// Draw the crushed plane on the map 
				Tower.planeHasCrashed(plane.getPlaneID());
				Tower.getInstance().getPlanes().remove(plane);
				Modes.reOrganiseChronos();
				socket.close();
			} catch (IOException e1) {
				System.out.println("Connection interrupted");
				e1.printStackTrace();
			}
		}

	}

}