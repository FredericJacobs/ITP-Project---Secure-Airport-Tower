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
 * Tower.java represents the tower of airport, which have the
 * function of receiving and sending Message with the planes.
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
	 */
	private static Tower instance;

	private static PriorityQueue<Message> inQueue;//
	private static KeyPair decryptKeypair;// the KeyPair for the tour
	private ArrayList <Plane> planes = new ArrayList<Plane>();
	private Journal journal = new Journal();
	private double consumption;
	private int passgerNumber;
	private int landingTimeTotal;


	private int landingPointX;
	private int landingPointY ;
	private ArrayList <Plane>landingRoute = new ArrayList<Plane>();
	private ArrayList <Plane>smallCircle = new ArrayList<Plane>();

	private int smallPointX;
	private int smallPointY;
	private int smallAngle;


	private ArrayList <Plane>middleCircle = new ArrayList<Plane>();
	private int middlePointX;
	private int middlePointY;
	private int middleAngle;


	private ArrayList <Plane>longCircle = new ArrayList<Plane>();

	private int longPointX;
	private int longPointY;
	private int longAngle;

	private int straightX;
	private int straightY;



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
		Tower.getInstance().getJournal().planeDidSendMayDay(planeID);
	}

	public static void planeHasCrashed (String planeID){
		Tower.getInstance().getJournal().planeHasCrashed(planeID);
	}
	
	public static void planDidLandSafely (String planeID){
		Tower.getInstance().getJournal().planeDidLand(planeID);
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
			publicKey.close();
			creatPriorityQueue();
			TourNetwork();

		} catch (FileNotFoundException e) {
			System.out.println("Key Not Found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TourNetwork is a method to open a socket connection server
	 * 
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("resource")
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
		this.consumption += consumption;
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
					this.setStraightX(Integer.parseInt(coordinates[0]));
					this.setStraightY(Integer.parseInt(coordinates[1]));
				break;
				
				case 'C':
					switch(circleName){
					case "smallcircle" : 
						this.setSmallPointX(Integer.parseInt(coordinates[0]));
						this.setSmallPointY(Integer.parseInt(coordinates[1]));
						this.setSmallAngle(Integer.parseInt(coordinates[2]));
						break;
					case "middlecircle" : 
						this.setMiddlePointX(Integer.parseInt(coordinates[0]));
						this.setMiddlePointY(Integer.parseInt(coordinates[1]));
						this.setMiddleAngle(Integer.parseInt(coordinates[2]));
						break;
					case "longcircle" : 
						this.setLongPointX(Integer.parseInt(coordinates[0]));
						this.setLongPointY(Integer.parseInt(coordinates[1]));
						this.setLongAngle(Integer.parseInt(coordinates[2]));
						break;	
					}
					break;
				
				case 'L':
					this.setLandingPointX(Integer.parseInt(coordinates[0]));
					this.setLandingPointY(Integer.parseInt(coordinates[1]));
					break;
					
				}
			}
			
			
		}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public ArrayList <Plane> getPlanes() {
		return planes;
	}

	public void setPlanes(ArrayList <Plane> planes) {
		this.planes = planes;
	}

	public int getPassgerNumber() {
		return passgerNumber;
	}

	public void setPassgerNumber(int passgerNumber) {
		this.passgerNumber += passgerNumber;
	}

	public int getLandingTimeTotal() {
		return landingTimeTotal;
	}

	public void setLandingTimeTotal(int landingTimeTotal) {
		this.landingTimeTotal += landingTimeTotal;
	}

	public int getLandingPointX() {
		return landingPointX;
	}

	public void setLandingPointX(int landingPointX) {
		this.landingPointX = landingPointX;
	}

	public int getSmallPointX() {
		return smallPointX;
	}

	public void setSmallPointX(int smallPointX) {
		this.smallPointX = smallPointX;
	}

	public int getSmallPointY() {
		return smallPointY;
	}

	public void setSmallPointY(int smallPointY) {
		this.smallPointY = smallPointY;
	}

	public int getSmallAngle() {
		return smallAngle;
	}

	public void setSmallAngle(int smallAngle) {
		this.smallAngle = smallAngle;
	}

	public int getMiddlePointX() {
		return middlePointX;
	}

	public void setMiddlePointX(int middlePointX) {
		this.middlePointX = middlePointX;
	}

	public int getMiddlePointY() {
		return middlePointY;
	}

	public void setMiddlePointY(int middlePointY) {
		this.middlePointY = middlePointY;
	}

	public int getMiddleAngle() {
		return middleAngle;
	}

	public void setMiddleAngle(int middleAngle) {
		this.middleAngle = middleAngle;
	}

	public int getLongPointX() {
		return longPointX;
	}

	public void setLongPointX(int longPointX) {
		this.longPointX = longPointX;
	}

	public int getLongPointY() {
		return longPointY;
	}

	public void setLongPointY(int longPointY) {
		this.longPointY = longPointY;
	}

	public int getLongAngle() {
		return longAngle;
	}

	public void setLongAngle(int longAngle) {
		this.longAngle = longAngle;
	}

	public int getStraightX() {
		return straightX;
	}

	public void setStraightX(int straightX) {
		this.straightX = straightX;
	}

	public int getStraightY() {
		return straightY;
	}

	public void setStraightY(int straightY) {
		this.straightY = straightY;
	}

	public int getLandingPointY() {
		return landingPointY;
	}

	public void setLandingPointY(int landingPointY) {
		this.landingPointY = landingPointY;
	}

	public ArrayList <Plane> getLandingRoute() {
		return landingRoute;
	}

	public void setLandingRoute(ArrayList <Plane> landingRoute) {
		this.landingRoute = landingRoute;
	}

	public ArrayList <Plane> getSmallCircle() {
		return smallCircle;
	}

	public void setSmallCircle(ArrayList <Plane> smallCircle) {
		this.smallCircle = smallCircle;
	}

	public ArrayList <Plane> getMiddleCircle() {
		return middleCircle;
	}

	public void setMiddleCircle(ArrayList <Plane> middleCircle) {
		this.middleCircle = middleCircle;
	}

	public ArrayList <Plane> getLongCircle() {
		return longCircle;
	}

	public void setLongCircle(ArrayList <Plane> longCircle) {
		this.longCircle = longCircle;
	}
}