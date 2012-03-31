package messaging;

import messaging.messages.Message;

import encryption.KeyPair;

/**
 ** This is the class of Plane. It is responsible for creat a array of the planes
 ** in the Tour to save all the information of the planes which have communicated
 ** with the Tour. It has several parameters.
 *
 ** @param planeID
 ** @param keypairo save the necessary Keypair for encrypted communication.
 ** @param messages
 *            The table of the messages that have been given and send.!! Not
 *            used yet for I can't find a way to initialize the abstract message
 ** @author Hantao Zhao
 ** @author Frederic Jacobs
 **/
public class Plane {

	private String planeID = "";
	private KeyPair keypair;
	private Message messages[];
	private int messageNo = 0;
	private int posX;
	private int posY;

	public Plane() {
	}

	/**
	 * The setter of the message, add one message to the array
	 * 
	 * @param mes The message that should be saved
	 * @return void
	 **/
	public void addMessage(Message mes) { // Unfinished , cant be used, We can't
											// initialize the abstract message
		messages[messageNo] = mes;
		messageNo++;
	}

	/**
	 * The getter of the message
	 * 
	 * @param mes The message that should be saved
	 * @return void
	 **/
	public Message[] getMessage() {
		return messages;
	}

	/**
	 * The setter and getter of the PlaneID
	 * 
	 * @param PlaneID The PlaneID that should be saved
	 */
	public void setPlaneID(String planeID) {
		this.planeID = planeID;
	}

	public String getPlaneID() {
		return planeID;
	}

	/**
	 * The setter and getter of the KeyPair
	 * 
	 * @param keyPair The KeyPairthat should be saved
	 */
	public void setKeypair(KeyPair keypair) {
		this.keypair = keypair;
	}

	public KeyPair getKeypair() {
		return keypair;
	}
	/**
	 * The setter and getter of the positions , X and Y. TBD: should we change the posx and posy into a table
	 * 
	 * @param keyPair The KeyPairthat should be saved
	 */
	public void setPosx(int posx) {
		this.posX = posx;
	}

	public void setPosy(int posy) {
		this.posY = posy;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

}
