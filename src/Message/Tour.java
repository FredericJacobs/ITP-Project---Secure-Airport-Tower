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

/** Description of Tour
 * Tour.java represents the tour of airport, which have the function of receiving and sending messages with the planes
 * By using the class of Messages, Tour can receive the Messages in the way that Print all the information to the 
 * file "Outfile.txt". And the information which is transfer can be shown as "Journal" in the Console
 * In this first version we haven't finished coding the RSA implementation
 * @author Hantao Zhao  
 * @author Frederic Jacobs
 * @version 1.0
 */
public class Tour

{
	/*
	 * Create a Singleton Pattern Because we only have one Tour so we need to
	 * initialize it only once
	 */

	private static Tour instance;
	public static PriorityQueue<Messages> priorityQueue ;

	public static Tour getInstance() {
		if (instance == null)
			instance = new Tour();
		return instance;
	}

	// Main function begins, but I still have doubt that some part of this
	// shouldn't appear in the "Tour" part.
	// While do we need to create a 'Plane'?
	public static void main(String agrs[]) throws IOException {
		priorityQueue = new PriorityQueue <Messages>(6, new Comparator<Messages>() {
			public int compare(Messages a, Messages b) {
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

		System.out.println("This is written for debugging purposes");
		System.out.println("Journal created"); // Print journal in the Console
		System.out.println("Priority      Type         Source       Destination         Date   ");
		
		String str = "a1000"; // A bad example to show how do we print and send the PlaneID, how to transfer and decodage the Byte[]?
		byte[] bytes = str.getBytes();
		// Three examples of Messages
		SendRSAKey send = new SendRSAKey(bytes, 0, 2, 2, 4, 5, bytes, 8, bytes);
		Mayday mayday = new Mayday(bytes, 1, 3, 3, "bird");
		Bye bye = new Bye(bytes);
		clearThetxtfile();
		receiveMessage(send);		
		receiveMessage(mayday);
		receiveMessage(bye);
		// Use java.util.PriorityQueue to put the messages in order 

	}

	public static void clearThetxtfile() { // clear the Outfile.txt file before
											// open it
		try {
			File f5 = new File("OutFile.txt");
			FileWriter fw5 = new FileWriter(f5);
			BufferedWriter bw1 = new BufferedWriter(fw5);
			bw1.write("");
		} catch (Exception e) {
		}

	}
	public static void receiveMessage(Messages messages) throws IOException{
		priorityQueue.offer(messages);	
		int queueSize = priorityQueue.size();
		for (int i = 0; i < queueSize; i++) {
			priorityQueue.poll().sendMessage(); // Send the messages to Outfile.txt
		}
	}
	public static void readThedata() throws FileNotFoundException{
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
		
	}

}
