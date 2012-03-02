package Message;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.lang.Enum;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/** Description of Message
 * Message is the file which contains the main class Messages. By using the abstract class Messages we build all the type
 * of messages we will use between Tour and planes.
 * So far the biggest issues we have is that how to print and transfer the PlaneID , which are in the form of Byte[]
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 */
enum MessageType {
	HELLO, DATA, MAYDAY, SENDRSA, CHOKE, UNCHOKE, BYE, ROUTING, KEEPALIVE, LANDINGREQUEST;
	// MessageType.DATA.ordinal(); To obtain the order number of MessageType
}

abstract public class Messages {

	protected byte[] planeID; // 8 octets (8 bytes)
	protected int length; // 0 sauf pour Data, MayDay et Routing Message
	protected int priority;
	protected int posx;
	protected int posy;
	protected MessageType type; // On envoie un int, MessageType est un nom
								// possible pour l¡¯Enum

	public Messages(byte[] planeID, int length, int priority, int posx,
			int posy, MessageType type) {
		this.planeID = planeID;
		this.length = length;
		this.priority = priority;
		this.posx = posx;
		this.posy = posy;
		this.type = type;
	}

	public int getPriority() {
		return this.priority;
	}
	// Show the date and time in the journal
	static class Datetime {
		public static String getDatetime_String1(){
			  String datetime=new Date().toString();
			  return datetime;
			 }
	}
	// Send the message 
	public void sendMessage() throws IOException {
		PrintWriter outStream;
		outStream = new PrintWriter(new FileWriter("OutFile.txt", true));
		outStream.println("**********************************************************");
		outStream.println("This is a < " + type + " >Message of Flight: "
				+ this.planeID[0] + ", located @ " + this.posx + " , "
				+ this.posy + ", length : " + this.length);
		outStream.close();
		System.out.println(priority+ "          " + type +  "         " +  " Plane     "  +   "Tour    " +   Datetime.getDatetime_String1() );

	}

}

class Hello extends Messages {
	private boolean crypted = false; // The conversation is crypted or not

	// TODO Auto-generated constructor stub

	public Hello(byte[] planeID, int length, int posx, int posy, boolean crypte) {
		super(planeID, length, 1, posx, posy, MessageType.HELLO);
		this.crypted = crypted;
	}
	// Override of the sendMessage, to add the additional information of each type of Messages
	public void sendMessage() throws IOException {
		super.sendMessage();
		PrintWriter outStream;
		outStream = new PrintWriter(new FileWriter("OutFile.txt", true));
		outStream.println("Crypted: " + crypted);
		outStream.close();
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
		PrintWriter outStream;
		outStream = new PrintWriter(new FileWriter("OutFile.txt", true));
		outStream.println("SendRSAKey:" + this.keySize + " modulusLength :"
				+ this.modulusLength + " Length : " + this.length);
		outStream.close();
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
		PrintWriter outStream;
		outStream = new PrintWriter(new FileWriter("OutFile.txt", true));
		outStream.println("RoutingMessageType : "  + TypeR + "MoveType : " + TypeM + "Payload : " + payload);
		outStream.close();
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
		PrintWriter outStream;
		outStream = new PrintWriter(new FileWriter("OutFile.txt", true));
		outStream.println("Cause : " + cause) ;
		outStream.close();
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
		outStream = new PrintWriter(new FileWriter("OutFile.txt", true));
		outStream.println("Hash : "  + hash + " Continuatuin : " + continuation + "Format : " + format + "FileSize : "+ fileSize + "Payload : " + payload);
		outStream.close();
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

	public Bye(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, 4, posx, posy, MessageType.BYE);
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
