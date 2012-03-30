package messaging;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
public class Tour{
	private static Tour instance;
	private static PriorityQueue<Message> inQueue;
	private static PriorityQueue<Message> outQueue;
	private static KeyPair decryptKeypair;
	private static int keepaliveX;
	private static int keepaliveY;
	private Journal journal;

	public static Tour getInstance() {
		if (instance == null)
			instance = new Tour();
		return instance;
	}

	public Tour() {
		inQueue = new PriorityQueue<Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.compareTo(b);
			}
		});

		outQueue = new PriorityQueue<Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.compareTo(b);
			}
		});
	}
	public static void setDecryptKeypair(KeyPair decrypt){
	    decryptKeypair = decrypt;
	}
	
	public static KeyPair getDecryptKeypair(){
		return decryptKeypair;
	}

	public static  void setkeepaliveX(int posx){
		keepaliveX = posx;
	}
	public static void setkeepaliveY(int posy){
		keepaliveY = posy;
	}


	public static void addMessageToOutgoingQueue(Message message) {
		outQueue.offer(message);
	}

	public static void addMessageToIncomingQueue(Message message) {
		inQueue.offer(message);
	}

	public static Message getNextMessageOutgoingQueue() {
		return outQueue.poll();
	}

	public static Message getNextMessageIncomingQueue() {
		return inQueue.poll();
	}
	public static void creatPriorityQueue(){
		inQueue = new PriorityQueue<Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.compareTo(b);
			}
		});
	}

	public static void main(String[] args) throws IOException,
			CloneNotSupportedException {
		creatPriorityQueue();
		TourNetwork();
		decryptKeypair = KeyGenerator.generateRSAKeyPair(256);
	}

	public static void TourNetwork() throws IOException,
			CloneNotSupportedException {
		ServerSocket serverSocket = null;
		DataOutputStream outData = null;
		DataInputStream inData = null;
		// Begin to connect by the net work socket , using the port "LOCALHOST",
		// 6900
	/*	addMessageToIncomingQueue(new ByeMessage("1553".getBytes(), 0, 20, 10));
		addMessageToIncomingQueue(new HelloMessage("hello1".getBytes(), 20, 10, (byte) 0));
		addMessageToIncomingQueue(new HelloMessage("hello2".getBytes(), 20, 10, (byte) 0));
		addMessageToIncomingQueue(new ByeMessage("1778".getBytes(), 0, 20, 10));
		System.out.println("queuesize" + inQueue.size());
		int n = inQueue.size();
		for (int i = 0; i < n;i++){
		getNextMessageIncomingQueue().print();
		System.out.println("queuesize = " + i);
		}*/
		try {
			serverSocket = new ServerSocket(6969);
		} catch (IOException e) {
			System.err.println("Could not listen on port");
			System.exit(1);
		}
		while(true){
			new TourThread(serverSocket.accept()).start();
		}
		/*
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}
		// new TourServerThread(serverSocket.accept()).start();

		outData = new DataOutputStream(clientSocket.getOutputStream());
		inData = new DataInputStream(clientSocket.getInputStream());
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);*/
		// Connection finished
		// this part is the main function to get and respond messages from or to
		// the plane
		// Recent issue: how to get continuing coming messages?
		// while (inData!= null) doesnt work for it doesnt return the message
		//
		/*
		 * new Thread(new Runnable() { public void run() { while (true) {
		 * while(true) { getNextMessageIncomingQueue().print();
		 * getNextMessageIncomingQueue().write(outData); try {
		 * Thread.sleep(1000); } catch (InterruptedException e) { } } } }
		 * }).start();
		 * 
		 * new Thread(new Runnable() { public void run() { while (true) {
		 * Message mes = ReadMessages.readMessage(inData);
		 * addMessageToIncomingQueue(mes); } } }).start();
		 */
		
/* [ the trying of using the queue] try {
			boolean flag = true;
			while (flag) {
				Message mes = ReadMessages.readMessage(inData);		
				addMessageToIncomingQueue(mes);
				if(mes.getType()==6){
					flag = false;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Cant hear anything from the plane,oops...");
			e.printStackTrace();
		}
		int inQueuesize = inQueue.size();
		for (int i =0; i< inQueuesize;i++){
			Message handleMessage = inQueue.poll();
		if (handleMessage.getType() != 6) {
			handleMessage.print();
			respond(handleMessage,outData);
		} else {
			handleMessage.print();
			respond(handleMessage, outData);
			System.out.println("Bye! Bon voyage");
			break;
		}
		}*/
	/*		while (true) {
				Message mes = ReadMessages.readMessage(inData);		
				if (mes.getType() != 6) {
					mes.print();
					respond(mes,outData);
				} else {
					mes.print();
					respond(mes,outData);
					System.out.println("Bye! Bon voyage");
					break;
				}
			}
		// finish the network and close the tunnel
		out.close();
		clientSocket.close();
		
		*/
		//	serverSocket.close();

	}}

	// respond to different type of message. Identify them by the method
	// getType(). Partly functioning

