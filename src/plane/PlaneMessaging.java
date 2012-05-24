package plane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import messaging.ReadMessages;
import messaging.messages.HelloMessage;
import messaging.messages.Message;
import encryption.RsaInputStream;
import encryption.RsaOutputStream;

public class PlaneMessaging implements Runnable {
	private PlaneMessageHandler messageHandler = new PlaneMessageHandler();
	private Message mes = null;
	private static DataInputStream in;
	private static DataOutputStream out;
	private static PlaneNavigation navigationThread = new PlaneNavigation();
	private static int encryptionStatus;

	@Override
	public void run() {

		in = TestPlane.getIn();
		out = TestPlane.getOut();
		HelloMessage HelloMessage = new HelloMessage(TestPlane.getPlaneID(), 0,0, TestPlane.isEncryptionEnabledAtLaunch() ? (byte) (1 << 4): (byte) 0);
		try {
			HelloMessage.write(out);
		} catch (IOException e1) {
			System.out.println("No outputStream for HelloMessage");
		}

		while (true) {
			try {
				mes = ReadMessages.readMessage(in);
				if (mes.getType() == 0) {
					new Thread(navigationThread).start();
					HelloMessage message = (HelloMessage) mes;
					if (message.isCrypted()) {
						out = new DataOutputStream(new RsaOutputStream(out,
								TestPlane.getEncryptKeypair()));
						System.out.println("ENCRYPTING");
					}
				}

				if (mes.getType() != 6) {
					encryptionStatus = (messageHandler.respond(mes, out));
					switch (encryptionStatus) {
					case 0:
						break;
					case 1:
						in = new DataInputStream(new RsaInputStream(in,
								TestPlane.getDecryptKeypair()));
						System.out.println("DECRYPTING");
						break;
					}
				}

				else {
					// Handle the bye message and stop reading from the plane
					messageHandler.respond(mes, out);
					System.out.println("Bye! Bon voyage");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static DataOutputStream getOutputStream() {
		return out;
	}
}