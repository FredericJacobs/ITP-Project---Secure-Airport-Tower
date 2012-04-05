package messaging;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
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

	public static PriorityQueue<Message> inQueue;//
	private static KeyPair decryptKeypair;// the KeyPair for the tour
	public static Plane planes[] = new Plane[100];
	public static int planeCounter = 0;
	
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

	public Tower() {
		inQueue = new PriorityQueue<Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.compareTo(b);
			}
		});
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

	/**
	 * The main method of the tour. It will create a inqueue, open a socket sever
	 * connection and generates a decryptKeypair
	 * 
	 * @param args
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */
	public static void main(String[] args) throws IOException,
			CloneNotSupportedException {
		decryptKeypair = KeyGenerator.generateRSAKeyPair(256);
		
		File outputFile = new File ("MyKey");
		outputFile.delete() ;
		FileOutputStream publicKeyFile = new FileOutputStream("MyKey");
		DataOutputStream publicKey = new DataOutputStream(publicKeyFile);
		publicKey.writeInt(decryptKeypair.getKeySize());
		publicKey.writeInt(decryptKeypair.getModulus().length);
		publicKey.write(decryptKeypair.getModulus());
		publicKey.writeInt(decryptKeypair.getPublicKey().length);
		publicKey.write(decryptKeypair.getPublicKey());
		creatPriorityQueue();
		TourNetwork();
		
	}

	/**
	 * TourNetwork is a method to open a socket connection server
	 * 
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */
	public static void TourNetwork() throws IOException,
			CloneNotSupportedException {
		ServerSocket serverSocket = null;
		// Begin to connect by the net work socket , using the port "LOCALHOST",
		// 6900
		try {
			serverSocket = new ServerSocket(6969);
		} catch (IOException e) {
			System.err.println("Could not listen on port");
			System.exit(1);
		}
		while (true) {
			new TowerThread(serverSocket.accept()).start();// call the TourThread
															// class
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public Plane[] getPlanes() {
		// TODO Auto-generated method stub
		return null;
	}

}
