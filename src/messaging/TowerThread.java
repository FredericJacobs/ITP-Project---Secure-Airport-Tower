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

			TowerMessageHandler messageHandler = new TowerMessageHandler(); // create
																			// a
																			// TowerMessageHandler
																			// to
																			// respond
																			// the
																			// messages
																			// send
																			// by
																			// the
																			// planes
			plane = new Plane();
			Tower.getInstance().getPlanes().add(plane); // Created a new plane
														// by using the order
			plane.setSocket(this.socket);

			while (true) {
				mes = ReadMessages.readMessage(inData);
				// read the message send by the DataInputStream
				// Tower.addMessageToIncomingQueue(mes);// Add it into the
				// incomingQueue

				if (mes.getType() == 3) {
					SendRSAMessage message = (SendRSAMessage) mes;
					plane.setKeypair(message.getPublicKey());
				}

				if (mes.getType() != 6) {
					// Handle the message , if the messageType isnt Bye, then go
					// to the next;Tower.getNextMessageIncomingQueue()

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

				} else {
					// Handle the bye message and stop reading from the plane
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
				System.out.println("One Plane lost,too bad");
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