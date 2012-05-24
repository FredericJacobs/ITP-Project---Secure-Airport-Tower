package plane;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.messages.DataMessage;
import messaging.messages.HelloMessage;
import messaging.messages.LandingMessage;
import messaging.messages.Message;
import messaging.messages.RoutingMessage;
import messaging.messages.SendRSAMessage;
import dataFile.DataFile;
import encryption.KeyPair;

/**
 * This class help the Plane to handle different type of messages
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class PlaneMessageHandler {

	DataFile towerDataFile = null;

	public PlaneMessageHandler() {
	}

	/**
	 * The respond method,it will respond a message and output the necessary
	 * information by sending it into the DataOutputStream
	 * 
	 * @param message
	 *            The message that need to be handled
	 * @param outData
	 *            The DataOutputStream where we send the feed back message
	 * @throws IOException
	 */
	public int respond(Message message, DataOutputStream outData)
			throws IOException {
		int type = message.getType();

		switch (type) {

		case 0:
			HelloMessage mes = (HelloMessage) message;
			if (mes.isCrypted()) {
				KeyPair publicKeyPair = TestPlane.getEncryptKeypair()
						.copyKeyPairWithoutPrivateKey();
				new SendRSAMessage(TestPlane.getPlaneID(),
						publicKeyPair.getKeySize(), Plane.getPosition()
						.getPosx(), Plane.getPosition().getPosy(),
						publicKeyPair).write(PlaneMessaging.getOutputStream());
				return 1;
			}

			new LandingMessage(TestPlane.getPlaneID(), 0, Plane.getPosition()
					.getPosx(), Plane.getPosition().getPosy())
			.write(PlaneMessaging.getOutputStream());

			if (TestPlane.getFileToSend() != null
					&& !TestPlane.getFileToSend().isEmpty()) {
				DataFile dataToSend = new DataFile(TestPlane.getFileToSend());
				dataToSend.writeToOutputStream(TestPlane.getPlaneIDString(),
						TestPlane.getOut());
			}

			return 0;

		case 1:
			if (towerDataFile == null) {
				towerDataFile = new DataFile("testfile", (DataMessage) message);
			}
			return 0;

		case 7:
			RoutingMessage routingMessage = (RoutingMessage) message;
			PlaneNavigation.currentInstruction = new RoutingInstruction(
					routingMessage.getPosition().getPosx(), routingMessage
					.getPosition().getPosy(), 0,
					routingMessage.getTypeM());
			System.out.println("New Instruction !");

		default:
			return 0;
		}
	}

}
