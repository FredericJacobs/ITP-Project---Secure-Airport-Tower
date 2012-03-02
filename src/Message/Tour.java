package Message;

import java.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.PriorityQueue;

import Message.*;

/** Description of Tour
 * Tour.java represents the tour of airport, which have the fonction of recieving and sending messages with the planes
 * By using the class of Messages, Tour can recieve the Messages in the way that Print all the information to the 
 * file "Outfile.txt". And the information which is transferd can be shown as "Journal" in the Console
 * In this first version we havnt finished coding the foncation of RSA
 * @author Hantao Zhao  
 * @author Frederic Jacobs
 * @version 1.0
 */
public class Tour

{
	/*
	 * Creat a Singleton Pattern Because we only have one Tour so we need to
	 * initialize it only once
	 */

	private static Tour instance;

	private Tour() {
		System.out.println("Tour created");
	}

	public static Tour getInstance() {
		if (instance == null)
			instance = new Tour();
		return instance;
	}

	// Main fonction begins, but I still have doube that some part of this
	// shoudnt appear in the "Tour" part.
	// While do we need to creat a 'Plane'?
	public static void main(String agrs[]) throws IOException {
		System.out.println("Hello!!!!!!!!!!!!");
		System.out.println("Journal created"); // Print journal in the Console
		System.out.println("Priority      Type         Source       Destination         Date   ");
		
		String str = "a1000"; // A bad example to show how do we print and send the PlaneID, how to transfer and decodage the Byte[]?
		byte[] bytes = str.getBytes();
		// Three examples of Messages
		SendRSAKey send = new SendRSAKey(bytes, 0, 2, 2, 4, 5, bytes, 8, bytes);
		Mayday mayday = new Mayday(bytes, 1, 3, 3, "bird");
		Bye bye = new Bye(bytes, 4, 4, 4);
		clearThetxtfile();
		// Use java.util.PriorityQueue to put the messages in order 
		PriorityQueue<Messages> priorityQueue = new PriorityQueue(6, new Comparator<Messages>() {
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
		// Send the messages to priorityQueue
		priorityQueue.offer(send);
		priorityQueue.offer(mayday);
		priorityQueue.offer(bye);
		int queueSize = priorityQueue.size();
		for (int i = 0; i < queueSize; i++) {
			priorityQueue.poll().sendMessage(); // Send the messages to Outfile.txt
		}

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

}
