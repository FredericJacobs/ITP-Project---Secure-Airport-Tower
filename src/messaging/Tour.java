package messaging;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

import encryption.*;
import Journal.Journal;
import messaging.messages.*;



/** Description of Tour
 * Tour.java represents the tour of airport, which have the function of receiving and sending Message with the planes
 * By using the class of Message, Tour can receive the Message in the way that Print all the information to the 
 * file "Outfile.txt". And the information which is transfer can be shown as "Journal" in the Console
 * In this first version we haven't finished coding the RSA implementation
 * @author Hantao Zhao  
 * @author Frederic Jacobs
 * @version 1.0
 */
public class Tour

{	private static Tour instance;
	private static PriorityQueue<Message> inQueue ;
	private static PriorityQueue<Message> outQueue ;
	private KeyPair decryptKeypair;
	private KeyPair encryptKeypair;
	private Journal journal;
	
	public static Tour getInstance() {
		if (instance == null)
			instance = new Tour();
		return instance;
	}

	public Tour (){
		
		inQueue = new PriorityQueue <Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.compareTo(b);
			}
		});
		
		
		outQueue = new PriorityQueue <Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.compareTo(b);
			}
		});
	}

	public static void addMessageToOutgoingQueue (Message message){
		outQueue.offer(message);
	}
	
	public static void addMessageToIncomingQueue (Message message){
		inQueue.offer(message);
	}
	
	public static Message getNextMessageOutgoingQueue (){
		return outQueue.poll();
	}
	
	public static Message getNextMessageIncomingQueue (){
		return inQueue.poll();
	}
}
