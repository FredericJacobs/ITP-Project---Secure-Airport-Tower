package Message;

import java.lang.Enum;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

enum MessageType {

	HELLO, DATA, MAYDAY, SENDRSA, CHOKE, UNCHOKE, BYE, ROUTING, KEEPALIVE, LANDINGREQUEST;
	// MessageType.DATA.ordinal(); To obtain the order number of MessageType
}

public class Messages {

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

	public void sendMessage() throws IOException {
		PrintWriter outStream;
		outStream = new PrintWriter(new FileWriter("OutFile.txt", true));
		outStream.println("This is a < " + type + " >Message of Flight: "
				+ this.planeID[0] + ", located @ " + this.posx + " , "
				+ this.posy + ", length : " + this.length + "    differed:");
		outStream.close();
	}

	public static void main(String agrs[]) throws IOException {
		System.out.println("Hello!!!!!!!!!!!!");
		String str = "a1000";
		byte[] bytes = str.getBytes();
		SendRSAKey send = new SendRSAKey(bytes, 0, 2, 2, 4, 5, bytes, 8, bytes);
		send.sendMessage();
	}
}

class Hello extends Messages {
	private boolean crypted = false; // The conversation is crypted or not

	// TODO Auto-generated constructor stub

	public Hello(byte[] planeID, int length, int posx, int posy, boolean crypte) {
		super(planeID, length, 1, posx, posy, MessageType.HELLO);
		this.crypted = crypted;
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
		outStream.println(" SendRSAKey:" + this.keySize + " modulusLength :"
				+ this.modulusLength + " Length : " + this.length);
		outStream.println("***************************");
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

}

class Mayday extends Messages {
	String cause;

	public Mayday(byte[] planeID, int length, int posx, int posy, String cause) {
		super(planeID, length, 0, posx, posy, MessageType.MAYDAY);
		this.cause = cause;
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
