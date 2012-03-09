package Message;

import java.util.Date;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// Enumeration of the different Message types

enum MessageType {
	HELLO, DATA, MAYDAY, SENDRSA, CHOKE, UNCHOKE, BYE, ROUTING, KEEPALIVE, LANDINGREQUEST;
	// MessageType.DATA.ordinal(); To obtain the order number of MessageType
}

/**
 * Description of Message Message is the file which contains the main class
 * Messages. By using the abstract class Messages we build all the type of
 * messages we'll send between planes and the control tower. All the sub-classes
 * are kept in a single Java file for clarity
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 */

abstract public class Messages {

	protected byte[] planeID;
	protected int length;
	protected int priority;
	protected int posx;
	protected int posy;
	protected MessageType type;

	/**
	 * Messages is an abstract constructor. It defines what Messages will have
	 * to implement.
	 * 
	 * @param planeID
	 *            An Array of Bytes storing the unique identifier of a plane
	 * @param length
	 *            Defines the length of Data, MayDay and the routing messages.
	 *            Should be zero for the rest of the Messages.
	 * @param priority
	 *            Defines the priority of a given message to be put in the
	 *            PriorityQueue
	 * @param posx
	 *            Gives the position of the plane on the x axis
	 * @param posy
	 *            Gives the position of the plane on the y axis
	 * @param type
	 *            Defines the type of the message
	 **/

	public Messages(byte[] planeID, int length, int priority, int posx,
			int posy, MessageType type) {
		this.planeID = planeID;
		this.length = length;
		this.priority = priority;
		this.posx = posx;
		this.posy = posy;
		this.type = type;
	}

	/**
	 * GetPriory is a getter method for the priority
	 * 
	 * @return Priority to be added in the PriorityQueue
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * Adding a time stamp to the Messages
	 * 
	 */
	static class Datetime {
		public static String getDatetime_String1() {
			String datetime = new Date().toString();
			return datetime;
		}
	}

	/**
	 * Method to a message
	 * 
	 * @throws IOException
	 *             Exception thrown if PrintWriter can't write to file
	 */
	public void sendMessage() throws IOException {
		// FileOutputStream outStream new FileOutputStream("OutFile.txt");
		String planeid = new String(planeID);
		/*
		 * outStream.println(
		 * "**********************************************************");
		 * outStream.println("This is a < " + type + " >Message of Flight: " +
		 * planeid + ", located @ " + this.posx + " , " + this.posy +
		 * ", length : " + this.length); outStream.close();
		 * System.out.println(priority+ "          " + type + "         " +
		 * " Plane     " + "Tour    " + Datetime.getDatetime_String1() );
		 */
		
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
		out.write(planeID);
		out.write(length);
		out.write(priority);
		out.write(posx);
		out.write(posy);
		out.write(type.ordinal());
		out.close();
	}

	public void writeMessage(ByteArrayOutputStream os) {
		// TODO Auto-generated method stub

	}

}

class Hello extends Messages {
	private byte crypted; // The conversation is crypted or not

	// TODO Auto-generated constructor stub

	public Hello(byte[] planeID, int posx, int posy, byte crypted) {
		super(planeID, 0, 1, posx, posy, MessageType.HELLO);
		this.crypted = crypted;
	}

	// Override of the sendMessage, to add the additional information of each
	// type of Messages
	public void sendMessage() throws IOException {
		super.sendMessage();
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
		out.write(crypted);
		out.close();
	}

}

class SendRSAKey extends Messages {

	int keySize;
	int modulusLength;
	byte[] modulus;
	int length;
	byte[] publicKey;

	public SendRSAKey(byte[] planeID, int length, int posx, int posy,
			int keySize, int modulusLength, byte[] modulus, int length2,
			byte[] publicKey) {
		super(planeID, length, 2, posx, posy, MessageType.SENDRSA);
		this.keySize = keySize;
		this.modulusLength = modulusLength;
		this.modulus = modulus;
		length = length2;
		this.publicKey = publicKey;
	}

	public void sendMessage() throws IOException {
		super.sendMessage();
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
		out.write(keySize);
		out.write(modulusLength);
		out.write(length);
		out.close();
	}

}

class RoutingMessage extends Messages {
	enum routingMessageType {
		NEWFIRST, LAST, REPLACEALL
	}

	enum moveType {
		STRAIGHT, CIRCULAR, LANDING, NONE, DESTRUCTION
	}

	routingMessageType TypeR;
	moveType TypeM;
	byte[] payload;

	public RoutingMessage(byte[] planeID, int length, int posx, int posy,
			routingMessageType typeR, moveType typeM, byte[] payload) {
		super(planeID, length, 2, posx, posy, MessageType.ROUTING);
		TypeR = typeR;
		TypeM = typeM;
		this.payload = payload;
		
	}

	public void sendMessage() throws IOException {
		super.sendMessage();

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
		out.write(TypeR.ordinal());
		out.write(TypeM.ordinal());
		out.write(payload);
		out.close();
	}

}

class Mayday extends Messages {
	String cause;

	public Mayday(byte[] planeID, int length, int posx, int posy, String cause) {
		super(planeID, length, 0, posx, posy, MessageType.MAYDAY);
		this.cause = cause;
	}

	public void sendMessage() throws IOException {
		super.sendMessage();
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
		out.write(cause.getBytes());
		out.close();
	}

}

class Data extends Messages {
	byte[] hash; // 20 octets (20 bytes)
	int continuation;
	byte[] format; // 4 octets (4 bytes)
	int fileSize;
	byte[] payload;



	public Data(byte[] planeID, int length, int posx, int posy, byte[] hash,
			int continuation, byte[] format, int fileSize, byte[] payload) {
		super(planeID, length, 4, posx, posy, MessageType.DATA);
		this.hash = hash;
		this.continuation = continuation;
		this.format = format;
		this.fileSize = fileSize;
		this.payload = payload;
	}

	public void sendMessage() throws IOException {
		super.sendMessage();
		PrintWriter outStream;
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
		out.write(hash);
		out.write(continuation);
		out.write(format);
		out.write(fileSize);
		out.write(payload);
		out.close();
	}
	public byte[] getHash() {
		return hash;
	}
}

class Choke extends Messages {

	public Choke(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 1, posx, posy, MessageType.CHOKE);
		// TODO Auto-generated constructor stub
	}

}

class Unchoke extends Messages {

	public Unchoke(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 4, posx, posy, MessageType.UNCHOKE);
		// TODO Auto-generated constructor stub
	}

}

class Bye extends Messages {

	public Bye(byte[] planeID) {
		super(planeID, 0, 4, 0, 0, MessageType.BYE);
		// TODO Auto-generated constructor stub
	}

}

class Keepalive extends Messages {

	public Keepalive(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 3, posx, posy, MessageType.KEEPALIVE);
		// TODO Auto-generated constructor stub
	}
}

class Landingrequest extends Messages {

	public Landingrequest(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 2, posx, posy, MessageType.LANDINGREQUEST);
		// TODO Auto-generated constructor stub
	}

}
