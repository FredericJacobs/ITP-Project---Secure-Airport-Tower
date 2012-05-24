package plane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import encryption.KeyGenerator;
import encryption.KeyPair;

/**
 ** TestPlane is the main class of our plane. It supports random name generation for planes, initialization with launch settings and generates the key used for encryption.
 **/
public class TestPlane {
	private static KeyPair decryptKeypair = KeyGenerator.generateRSAKeyPair(256);
	private static String planeID = generateString(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", 8);
	private static final int PLANE_UPDATE_INTERVAL = 100;
	private static boolean encryptionEnabledAtLaunch = false;
	private static String fileToSend = null;

	private static Socket socket = null;
	public static DataOutputStream out = null;
	public static DataInputStream in = null;
	private static String towerHost = "LOCALHOST";
	private static String towerPort = "6969";
	private static Plane plane = new Plane();
	private static KeyPair towerKey;

	public static String generateString(String characters, int length) {
		Random rng = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text).toUpperCase();
	}

	public static String getFileToSend() {
		return fileToSend;
	}

	public static void main(String[] args) throws IOException {
		init(args);

		try {
			socket = new Socket(towerHost, Integer.parseInt(towerPort));
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: LOCALHOST.");
			System.exit(1);
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to: LOCALHOST.");
			System.exit(1);
		}
		PlaneMessaging messagingThread = new PlaneMessaging();
		messagingThread.run();
	}

	private static void init(String[] args) {

		for (int i = 0; i < args.length; i++) {

			if (args[i].equals("--encryption-enabled")) {
				encryptionEnabledAtLaunch = (args[++i].equals("true"));
			}

			if (args[i].equals("--towerkey")) {

				FileInputStream publicKeyFile;
				try {
					publicKeyFile = new FileInputStream(args[++i]);
					DataInputStream publicKeyDIS = new DataInputStream(
							publicKeyFile);
					byte[] modulus = null;
					byte[] publicKey = null;

					int keySize = publicKeyDIS.readInt();
					@SuppressWarnings("unused")
					int modulusLength = publicKeyDIS.readInt();
					publicKeyDIS.read(modulus);
					@SuppressWarnings("unused")
					int publicKeyLength = publicKeyDIS.readInt();
					publicKeyDIS.read(publicKey);

					towerKey = new KeyPair(new BigInteger(modulus),
							new BigInteger(publicKey), null, keySize);

					publicKeyDIS.close();

				} catch (FileNotFoundException e) {
					System.out.println("File Not Found");
					System.exit(-1);
				} catch (IOException e) {

				}
			}

			if (args[i].equals("--towerhost")) {
				towerHost = (args[++i]);
			}

			if (args[i].equals("--towerport")) {
				towerPort = (args[++i]);
			}

			if (args[i].equals("--file-to-send")) {
				fileToSend = (args[++i]);
			}

			if (args[i].equals("--initialX")) {
				Plane.getPosition().setPosx(Integer.parseInt(args[++i]));
			}

			if (args[i].equals("--initialY")) {
				Plane.getPosition().setPosy(Integer.parseInt(args[++i]));
			}

			if (args[i].equals("--planeType")) {

				String planeTypeString = args[++i];

				if (planeTypeString.equalsIgnoreCase("A380")) {
					plane.changeTypeTo(PlaneType.A380);
				}

				if (planeTypeString.equalsIgnoreCase("A320")) {
					plane.changeTypeTo(PlaneType.A320);
				}

				if (planeTypeString.equalsIgnoreCase("B787")) {
					plane.changeTypeTo(PlaneType.B787);
				}

				if (planeTypeString.equalsIgnoreCase("CONCORDE")) {
					plane.changeTypeTo(PlaneType.CONCORDE);
				}

				if (planeTypeString.equalsIgnoreCase("GRIPEN")) {
					plane.changeTypeTo(PlaneType.GRIPEN);
				} else {
					System.out
							.println("Given plane type doesn't exist. Initialized with default A320");
				}
			}

			if (args[i].equals("--initialFuel")) {

				if (!plane.setFuelLevel(Double.parseDouble(args[++i]))) {
					System.out.println("The plane can't store that much fuel.");
					System.exit(-1);
				}

			}

		}
	}

	public static boolean isEncryptionEnabledAtLaunch() {
		return encryptionEnabledAtLaunch;
	}

	public static byte[] getPlaneID() {
		return planeID.getBytes();
	}

	public static String getPlaneIDString() {
		return planeID;
	}

	public static DataOutputStream getOut() {
		return out;
	}

	public static DataInputStream getIn() {
		return in;
	}

	public static int getPlaneUpdateInterval() {
		return PLANE_UPDATE_INTERVAL;
	}

	public static KeyPair getEncryptKeypair() {
		return towerKey;
	}

	public static KeyPair getDecryptKeypair() {
		return decryptKeypair;
	}

}
