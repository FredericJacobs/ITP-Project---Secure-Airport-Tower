package Message;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import Journal.Journal;

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
				int priorityA = a.getPriority();
				int priorityB = b.getPriority();
				if (priorityB > priorityA)
					return -1;
				else if (priorityB < priorityA)
					return 1;
				else
					return 0;
			}
		});
		
		
		outQueue = new PriorityQueue <Message>(6, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				int priorityA = a.getPriority();
				int priorityB = b.getPriority();
				if (priorityB > priorityA)
					return -1;
				else if (priorityB < priorityA)
					return 1;
				else
					return 0;
			}
		});
	}

	public static void receiveMessage(Message message){
		inQueue.offer(message);	
		int queueSize = inQueue.size();
		for (int i = 0; i < queueSize; i++) {
			inQueue.poll().sendMessage();
		}
	}
	
	public static void sendMessage (Message message) {
		
		
	}
		
	public static void readThedata() throws FileNotFoundException{
		DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
	}

}
