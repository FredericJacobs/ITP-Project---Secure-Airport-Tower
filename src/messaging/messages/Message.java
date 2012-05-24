package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;
import encryption.KeyPair;
import generals.XYPosition;

//Enumeration of the different Message types

enum MessageType {
	HELLO, DATA, MAYDAY, SENDRSA, CHOKE, UNCHOKE, BYE, ROUTING, KEEPALIVE, LANDINGREQUEST;
	// MessageType.DATA.ordinal(); To obtain the order number of MessageType
}

/**
 * Message is the file which contains the main class Message. By using the
 * abstract class Message we build all the type of Message we'll send between
 * planes and the control tower. All the sub-classes are kept in a single Java
 * file for clarity
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 */
public abstract class Message implements Comparable<Message>, Cloneable,
		VisitorMessage {
	protected byte[] planeID;
	protected int length;
	protected int priority;
	protected int posx;
	protected int posy;
	protected MessageType type;
	protected KeyPair aKeyPair;
	public long timeCreated;

	/**
	 * Message is an abstract constructor. It defines what Message will have to
	 * implement.
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
		this.timeCreated = System.currentTimeMillis();
	}

	/**
	 * Getter of the Priority
	 * 
	 * @return void
	 **/
	public int getPriority() {
		return this.priority;
	}

	/**
	 * The compare method for the priorityQueue
	 * 
	 * @return int
	 **/
	@Override
	public int compareTo(Message msg) {
		if (this.getPriority() != msg.getPriority())
			return this.getPriority() - msg.getPriority();
		// If the priority is the same then we compare the time the message is
		// created
		else
			return (int) (this.timeCreated - msg.timeCreated);

	}

	/**
	 * Print the current message in the Console
	 * 
	 * @return void
	 **/
	public void print() {
		String str = new String(planeID);
		if (planeID != null) {
			System.out.println("Type:" + type.toString());
			System.out.println("PlaneId :" + str);
			System.out.println("posx: " + posx);
			System.out.println("posy: " + posy);
		} else {
			System.out.println("Empty Message");
		}
	}

	/**
	 * Getter of the planeID
	 * 
	 * @return void
	 **/
	public XYPosition getPosition() {
		return new XYPosition(posx, posy);
	}

	public String getPlaneID() {
		String str = new String(planeID);
		return str;
	}

	public int getType() {
		return type.ordinal();
	}

	/**
	 * To send the message through DataOutputStream, in a given order
	 * 
	 * @return void
	 **/
	public void write(DataOutputStream out) throws IOException {
		out.write(planeID);
		out.writeInt(length);
		out.writeInt(priority);
		out.writeInt(posx);
		out.writeInt(posy);
		out.writeInt(type.ordinal());
	}

	public static String messageTypeName(int i) {
		String Type = null;
		switch (i) {
		case 0:
			Type = "HELLO";
			break;
		case 1:
			Type = "DATA";
			break;
		case 2:
			Type = "MAYDAY";
			break;
		case 3:
			Type = "SENDRSA";
			break;
		case 4:
			Type = "CHOKE";
			break;
		case 5:
			Type = "UNCHOKE";
			break;
		case 6:
			Type = "BYE";
			break;
		case 7:
			Type = "ROUTING";
			break;
		case 8:
			Type = "KEEPALIVE";
			break;
		case 9:
			Type = "LANDINGREQUEST";
			break;
		}
		return Type;
	}

	@Override
	public int accept(Visitor visitor, Plane plane, DataOutputStream outData) {
		return 0;
	}

}
