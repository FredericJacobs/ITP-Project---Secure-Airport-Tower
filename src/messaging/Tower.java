package messaging;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

import encryption.*;

//import encryption.KeyPair;
import messaging.messages.*;

/**
 * Description of Tour Tour.java represents the tour of airport, which have the
 * function of receiving and sending Message with the planes By using the class
 * of Message, Tour can receive the Message in the way that Print all the
 * information to the file "Outfile.txt". And the information which is transfer
 * can be shown as "Journal" in the Console In this first version we haven't
 * finished coding the RSA implementation
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 */
public class Tower implements Runnable {
	/**
	 * To make sure that the tour is unique we create the Singleton Pattern by
	 * using the instance and getInstance(). Thus it is impossible to use the
	 * constructor of the tour. That is to say that if we want the tower to use
	 * some method we need to write it in the main method.
	 * 
	 * @param inQueue
	 *            The PriorityQueue for the tour to save the not-handled
	 *            messages.Once the messages in, we can use the
	 *            getNextMessageIncomingQueue() to pull them out, by the order
	 *            of the priority
	 * @param KeyPair
	 *            The public Key of the Tour
	 * @param Journal
	 *            The journal of the tour
	 * @param planes
	 *            [] The array of the planes connected with the tour
	 * @param planeCounter
	 *            The static integer to count the number of the plane. Once a
	 *            plane is connected, it will plus 1.
	 */
	private static Tower instance;

	private static PriorityQueue<Message> inQueue;//
	private static KeyPair decryptKeypair;// the KeyPair for the tour
	public static ArrayList <Plane> planes = new ArrayList<Plane>();
	public static Journal journal = new Journal();
	public static double consumption;
	public static int passgerNumber;
	public static int landingTimeTotal;


	public static int landingPointX = 533;
	public  static int landingPointY = 437;
	public static ArrayList <Plane>landingRoute = new ArrayList<Plane>();
	public static ArrayList <Plane>smallCircle = new ArrayList<Plane>();

	public static int smallPointX = 350;
	public static int smallPointY = 100;
	public static int smallAngle = 36000;


	public static ArrayList <Plane>middleCircle = new ArrayList<Plane>();
	public static int middlePointX= 200;
	public static int middlePointY= 550;
	public static int middleAngle= 36000;


	public static ArrayList <Plane>longCircle = new ArrayList<Plane>();

	public static int longPointX= 700;
	public static int longPointY= 200;
	public static int longAngle= 36000;

	public static int straightX=420;
	public static int straightY=166;



	/**
	 * The functional method for Singleton Pattern
	 * 
	 * @return
	 */
	public static Tower getInstance() {
		if (instance == null)
			instance = new Tower();
		return instance;
	}

	private Tower() {
	}
	
	public static void planeDidSendMayDay (String planeID){
		journal.planeDidSendMayDay(planeID);
	}

	public static void planeHasCrashed (String planeID){
		journal.planeHasCrashed(planeID);
	}
	
	public static void planDidLandSafely (String planeID){
		journal.planeDidLand(planeID);
	}
	/**
	 * The setter and getter of the Keypair
	 * 
	 * @param decrypt
	 */
	public static void setDecryptKeypair(KeyPair decrypt) {
		decryptKeypair = decrypt;
	}

	public static KeyPair getDecryptKeypair() {
		return decryptKeypair;
	}

	/**
	 * The method to add or poll a message into the incoming queue of the tour
	 * 
	 * @param message
	 *            The incoming message
	 */
	public static void addMessageToIncomingQueue(Message message) {
		inQueue.offer(message);
	}

	public static Message getNextMessageIncomingQueue() {
		return inQueue.poll();
	}

	/**
	 * This method created the inQueue of the tour. It will be called in the
	 * main method
	 */
	public static void creatPriorityQueue() {
		inQueue = new PriorityQueue<Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.compareTo(b);
			}
		});
	}

	public static void deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	        	(new File ("downloads"+ File.separator + children[i])).delete();
	        }
	    }

	}
	
	public void run (){
		setCircle("landing");
		setCircle("smallcircle");
		setCircle("middlecircle");
		setCircle("longcircle");

		decryptKeypair = KeyGenerator.generateRSAKeyPair(256);

		File outputFile = new File ("MyKey");
		outputFile.delete() ;
		File downloads = new File("downloads");
		deleteDir(downloads);
		
		FileOutputStream publicKeyFile;
		
		try {
			//TowerMessageHandler messageHandler = new TowerMessageHandler();
			publicKeyFile = new FileOutputStream("MyKey");
			DataOutputStream publicKey = new DataOutputStream(publicKeyFile);
			publicKey.writeInt(decryptKeypair.getKeySize());
			publicKey.writeInt(decryptKeypair.getModulus().length);
			publicKey.write(decryptKeypair.getModulus());
			publicKey.writeInt(decryptKeypair.getPublicKey().length);
			publicKey.write(decryptKeypair.getPublicKey());
			creatPriorityQueue();
			TourNetwork();

		} catch (FileNotFoundException e) {
			System.out.println("Key Not Found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * TourNetwork is a method to open a socket connection server
	 * 
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */
	public static void TourNetwork()
	{
		ServerSocket serverSocket = null;
		// Begin to connect by the net work socket , using the port "LOCALHOST",
		// 6969
		try {
			serverSocket = new ServerSocket(6969);
		} catch (IOException e) {
			System.err.println("Could not listen on port");
			System.exit(1);
		}
		while (true) {
			try {
				new TowerThread(serverSocket.accept()).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// call the
															// TourThread
															// class
		}

	}

	public double getConsumption() {
		return consumption;
	}

	public void setConsumption(double consumption) {
		Tower.consumption += consumption;
	}
	
	// This method allows the circle information to be transfered from .txt file into the tower 
	public void setCircle(String circleName){
			Scanner scanner = null;
			try {
				scanner = new Scanner(new FileInputStream("Circle"+ File.separator + circleName +".txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			
			String tmp = scanner.nextLine();
			String instructions[] = tmp.split(";");
			String coordinates[] = null;
			
			for(String ins : instructions) {
				coordinates = ((String) ins.subSequence(1, ins.length())).split(",");
				switch(ins.charAt(0)) {
				case 'S':
					Tower.straightX= Integer.parseInt(coordinates[0]);
					Tower.straightY = Integer.parseInt(coordinates[1]);
				break;
				
				case 'C':
					switch(circleName){
					case "smallcircle" : 
						Tower.smallPointX = Integer.parseInt(coordinates[0]);
						Tower.smallPointY = Integer.parseInt(coordinates[1]);
						Tower.smallAngle =  Integer.parseInt(coordinates[2]);
						break;
					case "middlecircle" : 
						Tower.middlePointX = Integer.parseInt(coordinates[0]);
						Tower.middlePointY = Integer.parseInt(coordinates[1]);
						Tower.middleAngle =  Integer.parseInt(coordinates[2]);
						break;
					case "longcircle" : 
						Tower.longPointX = Integer.parseInt(coordinates[0]);
						Tower.longPointY = Integer.parseInt(coordinates[1]);
						Tower.longAngle =  Integer.parseInt(coordinates[2]);
						break;	
					}
					break;
				
				case 'L':
					Tower.longPointX = Integer.parseInt(coordinates[0]);
					Tower.longPointY = Integer.parseInt(coordinates[0]);
					break;
					
				}
			}
			
			
		}
}