package Message;

import java.util.Date;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// Enumeration of the different Message types

enum MessageType {
	HELLO, DATA, MAYDAY, SENDRSA, CHOKE, UNCHOKE, BYE, ROUTING, KEEPALIVE, LANDINGREQUEST;
	// MessageType.DATA.ordinal(); To obtain the order number of MessageType
}

/**
 * Message is the file which contains the main class
 * Message. By using the abstract class Message we build all the type of
 * Message we'll send between planes and the control tower. All the sub-classes
 * are kept in a single Java file for clarity
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 */

abstract public class Message {

	protected byte[] planeID;
	protected int length;
	protected int priority;
	protected int posx;
	protected int posy;
	protected MessageType type;

	/**
	 * Message is an abstract constructor. It defines what Message will have
	 * to implement.
	 * 
	 * @param planeID
	 *            An Array of Bytes storing the unique identifier of a plane
	 * @param length
	 *            Defines the length of Data, MayDay and the routing Message.
	 *            Should be zero for the rest of the Message.
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

	public Message(byte[] planeID, int length, int priority, int posx,
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
	 * Adding a time stamp to the Message
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

class Hello extends Message {
	private byte reserved; 
	// TODO Auto-generated constructor stub

	public Hello(byte[] planeID, int posx, int posy, byte crypted) {
		super(planeID, 0, 1, posx, posy, MessageType.HELLO);
		this.reserved = crypted;
	}

	// Override of the sendMessage, to add the additional information of each
	// type of Message
	public void sendMessage() throws IOException {
		super.sendMessage();
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream("OutFile.dat")));
		out.write(reserved);
		out.close();
	}

}

class SendRSAKey extends Message {

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

class RoutingMessage extends Message {
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

class Mayday extends Message {
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

class Data extends Message {
	byte[] hash;
	int continuation;
	byte[] format;
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

class Choke extends Message {

	public Choke(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 1, posx, posy, MessageType.CHOKE);
		// TODO Auto-generated constructor stub
	}

}

class Unchoke extends Message {

	public Unchoke(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 4, posx, posy, MessageType.UNCHOKE);
		// TODO Auto-generated constructor stub
	}

}

class Bye extends Message {

	public Bye(byte[] planeID) {
		super(planeID, 0, 4, 0, 0, MessageType.BYE);
		// TODO Auto-generated constructor stub
	}

}

class Keepalive extends Message {

	public Keepalive(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 3, posx, posy, MessageType.KEEPALIVE);
		// TODO Auto-generated constructor stub
	}
}

class Landingrequest extends Message {

	public Landingrequest(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 2, posx, posy, MessageType.LANDINGREQUEST);
		// TODO Auto-generated constructor stub
	}

}
