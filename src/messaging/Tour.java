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
	// private KeyPair decryptKeypair;
	// private KeyPair encryptKeypair;
	private Journal journal;
	private KeyPair publicKey;
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

		portTour();
	}

	public static void portTour() throws IOException {
		ServerSocket serverSocket = null;
		DataOutputStream outData = null;
		DataInputStream inData = null;
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
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		String inputLine, outputLine;
		// HelloProtocol kkp = new HelloProtocol();
		// outputLine = kkp.processInput(null);
		//out.println("Tour of F.H checked, please send a message");
	//	System.out.println("----Messages from the plane-----");
		Message mes = ReadMessages.readMessage(inData);
		mes.print();
		repond(mes).write(outData);
		
		/*
		 * while ((inputLine = in.readLine()) != null) { outputLine =
		 * kkp.processInput(inputLine); out.println(outputLine);
		 * out.println(inData);// Return the first letter
		 * ReadMessages.readMessage(inData).print(); if
		 * (outputLine.equals("Bye.")) break; }
		 */
		out.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
	}
	
	public static Message repond(Message message){
		int type = message.getType();
		switch (type) {
		case 0:
			return  new HelloMessage("Tour".getBytes(), 0, 0, (byte) 0);
		case 1: 
	//		publicKey = (SendRSAMessage)message.getPublicKey();
		default:
			return null;
		}

		
	}
}

class HelloProtocol {
	private static final int WAITING = 0;
	private static final int SENTKNOCKKNOCK = 1;
	private static final int SENTCLUE = 2;
	private static final int ANOTHER = 3;
	private static final int NUMJOKES = 5;

	private int state = WAITING;
	private int currentJoke = 0;

	/*
	 * public String processInput(String theInput) { String theOutput = null;
	 * theOutput = "Plane connected, please send a messsage"; if
	 * (theInput.equalsIgnoreCase("hello")) { theOutput = "Hello you!"; state =
	 * SENTCLUE; } else { // theOutput = null; } return theOutput; }
	 */

	private String[] clues = { "Turnip", "Little Old Lady", "Atch", "Who",
			"Who" };
	private String[] answers = { "Turnip the heat, it's cold in here!",
			"I didn't know you could yodel!", "Bless you!",
			"Is there an owl in here?", "Is there an echo in here?" };

	public String processInput(String theInput) {
		String theOutput = null;

		if (state == WAITING) {
			theOutput = "Plane connected, please send a messsage";
			state = SENTKNOCKKNOCK;
		} else if (state == SENTKNOCKKNOCK) {
			if (theInput.equalsIgnoreCase("Hello")) {
				theOutput = clues[currentJoke];
				state = SENTCLUE;
			} else {
				theOutput = "You're supposed to say \"Hello\"! "
						+ "Try again. Knock! Knock!";
			}
		} else if (state == SENTCLUE) {
			if (theInput.equalsIgnoreCase(clues[currentJoke] + " who?")) {
				theOutput = answers[currentJoke] + " Want another? (y/n)";
				state = ANOTHER;
			} else {
				theOutput = "Bye.";
				state = WAITING;
			}/*
			 * else { theOutput = "You're supposed to say \"" +
			 * clues[currentJoke] + " who?\"" + "! Try again. Knock! Knock!";
			 * state = SENTKNOCKKNOCK; } } else if (state == ANOTHER) { if
			 * (theInput.equalsIgnoreCase("y")) { theOutput = "Knock! Knock!";
			 * if (currentJoke == (NUMJOKES - 1)) currentJoke = 0; else
			 * currentJoke++; state = SENTKNOCKKNOCK; } else { theOutput =
			 * "Bye."; state = WAITING; } }
			 */
		}
		return theOutput;
	}
}