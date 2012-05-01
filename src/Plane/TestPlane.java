package Plane;

import java.io.BufferedReader;

import messaging.ReadMessages;
import messaging.messages.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import encryption.KeyGenerator;
import encryption.KeyPair;
import generals.XYPosition;


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
	private static XYPosition planePosition;
	private static File encryptionKey = null;
	private static Socket kkSocket = null;
	private static PrintWriter out = null;
	private static BufferedReader in = null;
	private static DataOutputStream outData = null;
	private static DataInputStream inData = null;
	private static String towerHost = "LOCALHOST";
	private static String towerPort = "6969";
	
	
	public static void main(String[] args) throws IOException {
		
		init(args);
		// Begin to connect by the net work socket , using the port "LOCALHOST",
		// 6900
		try {
			kkSocket = new Socket(towerHost, Integer.parseInt(towerPort));
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));

			outData = new DataOutputStream(kkSocket.getOutputStream());
			inData = new DataInputStream(kkSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: LOCALHOST.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: LOCALHOST.");
			System.exit(1);
		}
		// begin to transfer messages
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
				hello.write(outData);
				System.out.println("----Messages from the tour-----");
				ReadMessages.readMessage(inData).print();
				break;
			case 1: 
			case 2:
			case 3: 
				// Encryption Support broken in this test. Use given planes
				SendRSAMessage sendRSA = new SendRSAMessage(planeID.getBytes(),8, 20, 10,decryptKeypair);
				sendRSA.write(outData);
				ReadMessages.readMessage(inData).print();
				break;
			case 6:
				ByeMessage bye = new ByeMessage(planeID.getBytes(), 0, 20, 10);
				bye.write(outData);
				System.out.println("----Messages from the tour-----");
				ReadMessages.readMessage(inData).print();
				System.out.println("Bye! Bon voyage!");
				break;
			case 8:
				KeepAliveMessage KeepAlive = new KeepAliveMessage(planeID.getBytes(), 20, 10);
				KeepAlive.write(outData);
				System.out.println("----Messages from the tour-----no return message");
				break;
			}
		}
		out.close();
		in.close();
		kkSocket.close();
	}

	private static void init(String[] args) {
		planePosition = new XYPosition();
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
				planePosition.setPosx(Integer.parseInt(args[++i]));
			}
			
			if (args[i].equals("--initialY")){
				planePosition.setPosy(Integer.parseInt(args[++i]));
			}
			
			if 
		}
	}
}
