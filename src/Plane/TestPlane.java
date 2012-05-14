package plane;

import messaging.ReadMessages;
import messaging.messages.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

import encryption.KeyGenerator;
import encryption.KeyPair;


/**
 ** This class the a test plane for our first step of the socket programming. Since we will use the .jar file to model the planes we dont need this class for now
 *
 ** But just in case we keep it in to try some test also to fulfill the demand of the mid-term check
 **/
public class TestPlane {

	/**
	 * @param args
	 */
	private static KeyPair decryptKeypair= KeyGenerator.generateRSAKeyPair(256);
	private static String planeID = "B1778000"; //Fixed for debugging purposes
	private static final int PLANE_UPDATE_INTERVAL = 100 ; 
	private static boolean encryptionEnabledAtLaunch;
	private static File encryptionKey = null;
	private static Socket socket = null;
	public static DataOutputStream out = null;
	public static DataInputStream in = null;
	private static String towerHost = "LOCALHOST";
	private static String towerPort = "6969";
	private static Plane plane = new Plane ();
	private static PriorityQueue<Message> inQueue;
	
	public static void addMessageToIncomingQueue(Message message) {
		inQueue.offer(message);
	}
	
	public static Message getNextMessageIncomingQueue() {
		return inQueue.poll();
	}
	
	public static void main(String[] args) throws IOException {
		
		inQueue = new PriorityQueue<Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.compareTo(b);
			}
		});
		
		init(args);
		
		try {
			socket = new Socket(towerHost, Integer.parseInt(towerPort));
			out = new DataOutputStream(
					socket.getOutputStream());
			in = new DataInputStream(
					socket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: LOCALHOST.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: LOCALHOST.");
			System.exit(1);
		}
		PlaneMessaging messagingThread = new PlaneMessaging();
		messagingThread.run();
	}

	private static void init(String[] args) {
		
		for (int i = 0; i< args.length; i++){
			
			if (args[i].equals("--encryption-enabled")){
				encryptionEnabledAtLaunch = (args[++i].equals("true"));
			}
			
			if (args[i].equals("--towerkey")){
				System.out.println("This key may not be up-to-date, please fetch it securely from the tower");
				System.exit(-1);
			}
			
			if (args[i].equals("--towerhost")){
				towerHost = (args[++i]);
			}
			
			if (args[i].equals("--towerport")){
				towerPort = (args[++i]);
			}
			
			if (args[i].equals("--file-to-send")){
				//NOT IMPLEMENTED YET
			}
			
			if (args[i].equals("--initialX")){
				Plane.getPosition().setPosx(Integer.parseInt(args[++i]));
			}
		
			if (args[i].equals("--initialY")){
				Plane.getPosition().setPosy(Integer.parseInt(args[++i]));
			}
			
			if (args[i].equals("--planeType")){
				
				String planeTypeString = args [++i];
				
				if (planeTypeString.equalsIgnoreCase("A380")){
					plane.changeTypeTo(PlaneType.A380);
				}
				
				if (planeTypeString.equalsIgnoreCase("A320")){
					plane.changeTypeTo(PlaneType.A320);
				}
				
				if (planeTypeString.equalsIgnoreCase("B787")){
					plane.changeTypeTo(PlaneType.B787);
				}

				if (planeTypeString.equalsIgnoreCase("CONCORDE")){
					plane.changeTypeTo(PlaneType.CONCORDE);
				}

				if (planeTypeString.equalsIgnoreCase("GRIPEN")){
					plane.changeTypeTo(PlaneType.GRIPEN);
				}
				else {
					System.out.println("Given plane type doesn't exist. Initialized with default A320");
				}
			}
			
			if (args[i].equals("--initialFuel")){
				
				if (!plane.setFuelLevel(Double.parseDouble(args[++i]))){
					System.out.println("The plane can't store that much fuel.");
					System.exit(-1);
				}
				
			}
			
		}
	}
	
	public static boolean isEncryptionEnabledAtLaunch() {
		return encryptionEnabledAtLaunch;
	}
	
	public static byte[] getPlaneID () {
		return planeID.getBytes();
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
	
}
