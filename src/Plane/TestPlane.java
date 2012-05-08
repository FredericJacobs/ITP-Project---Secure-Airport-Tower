package Plane;

import messaging.ReadMessages;
import messaging.messages.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
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
	private static String planeID = "B1778000";
	private static final int PLANE_UPDATE_INTERVAL = 100 ; 
	private static boolean encryptionEnabledAtLaunch;
	private static File encryptionKey = null;
	private static Socket socket = null;
	public static DataOutputStream out = null;
	public static DataInputStream in = null;
	private static String towerHost = "LOCALHOST";
	private static String towerPort = "6969";
	private static Plane plane = new Plane ();
	
	
	public static void main(String[] args) throws IOException {
		
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
		
		
		System.out.println("This is plane B1778000 please give instructions");
		System.out.println("0=HELLO, 1=DATA, 2=MAYDAY, 3=SENDRSA, 4=CHOKE, 5=UNCHOKE, 6=BYE,7=ROUTING, 8=KEEPALIVE, 9=LANDINGREQUEST");
		Scanner scanner = new Scanner(System.in);
		int i = 0;
		while (i != 6) {
			i = scanner.nextInt();
			switch (i) {
			case 0:
				HelloMessage hello = new HelloMessage(planeID.getBytes(), 20, 10, (byte) 0);
				hello.print();
				hello.write(out);
				System.out.println("----Messages from the tour-----");
				ReadMessages.readMessage(in).print();
				break;
			case 1: 
			case 2:
			case 3: 
				
				// Encryption Support broken in this test. Use given planes
				
				SendRSAMessage sendRSA = new SendRSAMessage(planeID.getBytes(),8, 20, 10,decryptKeypair);
				sendRSA.write(out);
				ReadMessages.readMessage(in).print();
				break;
			case 6:
				ByeMessage bye = new ByeMessage(planeID.getBytes(), 0, 20, 10);
				bye.write(out);
				System.out.println("----Messages from the tour-----");
				ReadMessages.readMessage(in).print();
				System.out.println("Bye! Bon voyage!");
				break;
			case 8:
				KeepAliveMessage KeepAlive = new KeepAliveMessage(planeID.getBytes(), plane.getPosition().getPosx(), plane.getPosition().getPosy());
				KeepAlive.write(out);
				System.out.println("----Messages from the tour-----no return message");
				break;
			}
		}
		out.close();
		in.close();
		socket.close();
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
				plane.getPosition().setPosx(Integer.parseInt(args[++i]));
			}
		
			if (args[i].equals("--initialY")){
				plane.getPosition().setPosy(Integer.parseInt(args[++i]));
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
}
