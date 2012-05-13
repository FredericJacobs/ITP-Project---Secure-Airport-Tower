package messaging;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

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

	private  static PriorityQueue<Message> inQueue;//
	private static KeyPair decryptKeypair;// the KeyPair for the tour
	public static Plane planes[] = new Plane[200];
	public static int planeCounter = 0;
	public static Journal journal = new Journal();

	public final static int landingPointX = 533;
	public final static int landingPointY = 437;
	public static ArrayList <Plane>landingRoute = new ArrayList<Plane>();
	public static ArrayList <Plane>smallCircle = new ArrayList<Plane>();

	public static int smallPointX = 350;
	public static int smallPointY = 100;
	public static int smallAngle = 3600;


	public static ArrayList <Plane>middleCircle = new ArrayList<Plane>();
	public static int middlePointX= 200;
	public static int middlePointY= 550;
	public static int middleAngle= 3600;


	public static ArrayList <Plane>longCircle = new ArrayList<Plane>();

	public static int longPointX= 700;
	public static int longPointY= 200;
	public static int longAngle= 3600;

	public static int straightX=400;
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

	public void run (){
		decryptKeypair = KeyGenerator.generateRSAKeyPair(256);

		File outputFile = new File ("MyKey");
		outputFile.delete() ;
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
			/*while(true){
				if(inQueue.size()!=0){
				Message mes = inQueue.remove();
				if (mes.getType() != 6) {            // Handle the message , if the messageType isnt Bye, then go to the next

					encryptionStatus = (messageHandler.respond(Tower.planes[planenumber], planenumber , Tower.getNextMessageIncomingQueue(), outData));
					switch (encryptionStatus){	
					case 0: break; 
					case 1: inData = new DataInputStream( new RsaInputStream(socket.getInputStream(), Tower.getDecryptKeypair()));System.out.println("DECRYPTING"); break;
					case 2: outData = new DataOutputStream(new RsaOutputStream(socket.getOutputStream(), Tower.planes[planenumber].getKeypair())); System.out.println("ENCRYPTING"); break;
					}

				} else {
					// Handle the bye message and stop reading from the plane

					messageHandler.respond(Tower.planes[planenumber],
							planenumber, Tower.getNextMessageIncomingQueue(), outData);
					System.out.println("Bye! Bon voyage");
					break;
				}
			
			}}*/
		} catch (FileNotFoundException e) {
			System.out.println("Key Not Found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The main method of the tour. It will create a inqueue, open a socket
	 * sever connection and generates a decryptKeypair
	 * 
	 * @param args
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */

	public static void main(String[] args) throws IOException, CloneNotSupportedException {
		 (new Thread(new Tower())).start();	
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


	public Plane[] getPlanes() {
		// TODO Auto-generated method stub
		return null;
	}

}