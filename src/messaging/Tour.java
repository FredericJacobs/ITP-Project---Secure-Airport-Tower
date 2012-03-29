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

import encryption.KeyPair;

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
public class Tour {
	private static Tour instance;
	private static PriorityQueue<Message> inQueue;
	private static PriorityQueue<Message> outQueue;
	private static KeyPair decryptKeypair;
	private static KeyPair encryptKeypair;
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

	public static void main(String[] args) throws IOException,
			CloneNotSupportedException {
		TourNetwork();
	}

	public static void TourNetwork() throws IOException,
			CloneNotSupportedException {
		ServerSocket serverSocket = null;
		DataOutputStream outData = null;
		DataInputStream inData = null;
		boolean listening = true;
		// Begin to connect by the net work socket , using the port "LOCALHOST",
		// 6900
		try {
			serverSocket = new ServerSocket(6969);
		} catch (IOException e) {
			System.err.println("Could not listen on port");
			System.exit(1);
		}
		Socket clientSocket = null;
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}
		// new TourServerThread(serverSocket.accept()).start();

		outData = new DataOutputStream(clientSocket.getOutputStream());
		inData = new DataInputStream(clientSocket.getInputStream());
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
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
		try {
			while (inData != null) {
				Message mes = ReadMessages.readMessage(inData);		
				if (mes.getType() != 6) {
					mes.print();
					respond(mes,outData);
				} else {
					mes.print();
					respond(mes, outData);
					System.out.println("Bye! Bon voyage");
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Cant hear anything from the plane,oops...");
			e.printStackTrace();
		}
		// finish the network and close the tunnel
		out.close();
		clientSocket.close();
		serverSocket.close();
	}

	// respond to different type of message. Identify them by the method
	// getType(). Partly functioning
	public static Message respond(Message message, DataOutputStream outData)
			throws IOException {
		int type = message.getType();
		switch (type) {
		case 0:
			System.out.println("respond hello");
			HelloMessage hello = new HelloMessage("Tour0000".getBytes(), 0, 0,
					(byte) 0);
		    hello.write(outData);
			return hello;
			/*
			 * if (((HelloMessage) message).isCrypted()) { return new
			 * HelloMessage("Tour0000".getBytes(), 0, 0, (byte) 1); } else {
			 * return new HelloMessage("Tour0000".getBytes(), 0, 0, (byte) 0); }
			 */
		case 1:
			// Data, save the file that received TDB
		case 2:// Mayday, future issue
		case 3:// SendRSA, unfinished for the keypair
			decryptKeypair = ((SendRSAMessage) message).getPublicKey();
			return null;
			// case 4,5,7 shouldnt happen to the tour
		case 6:
			System.out.println("Connection terminated");
			ByeMessage bye = new ByeMessage("Tour0000".getBytes(), 0, 0,(byte) 0);
			bye.write(outData);
			return bye;
		case 8:
			keepaliveX = ((KeepAliveMessage) message).keepaliveX();
			keepaliveY = ((KeepAliveMessage) message).keepaliveY();
			System.out.println("keepaliveX :" + keepaliveX);
			System.out.println("keepaliveY :" + keepaliveY);
			return null;
			// keep alive
		case 9: // Landing request, future issue
		default:
			return null;
		}
	}
}
