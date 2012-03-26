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
public class Tour

{
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

	public static void main(String[] args) throws IOException {
		TourNetwork();
	}

	public static void TourNetwork() throws IOException {
		ServerSocket serverSocket = null;
		DataOutputStream outData = null;
		DataInputStream inData = null;
		// Begin to connect by the net work socket , using the port "LOCALHOST",
		// 6900
		try {
			serverSocket = new ServerSocket(6900);
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
		outData = new DataOutputStream(clientSocket.getOutputStream());
		inData = new DataInputStream(clientSocket.getInputStream());
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		// Connection finished
		// this part is the main function to get and respond messages from or to
		// the plane
		// Recent issue: how to get continuing coming messages?

		// while (inData!= null) doesnt work for it doesnt return the message
		//
		Message mes = ReadMessages.readMessage(inData);
		mes.print();
		respond(mes).write(outData);
		// finish the network and close the tunnel
		out.close();
		clientSocket.close();
		serverSocket.close();
	}

	// respond to different type of message. Identify them by the method
	// getType(). Partly functioning
	public static Message respond(Message message) {
		int type = message.getType();
		switch (type) {
		case 0:
			if (((HelloMessage) message).isCrypted()) {
				return new HelloMessage("Tour0000".getBytes(), 0, 0, (byte) 1);
			} else {
				return new HelloMessage("Tour0000".getBytes(), 0, 0, (byte) 0);
			}
		case 1:
			//Data, save the file that recieved TDB
		case 2://Mayday, future issue
		case 3://SendRSA, unfinished for the keypair
			decryptKeypair = ((SendRSAMessage) message).getPublicKey();
		//case 4,5,7 shouldnt happen to the tour
		case 6:
			return new ByeMessage("Tour0000".getBytes(), 0, 0, 0);
		case 8: 
			keepaliveX = ((KeepAliveMessage)message).keepaliveX();
			keepaliveY = ((KeepAliveMessage)message).keepaliveY();
			// keep alive
		case 9: //Landing request, future issue
		default:
			return null;
		}
	}
}
