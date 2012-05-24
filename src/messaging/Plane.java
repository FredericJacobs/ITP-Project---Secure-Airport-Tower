package messaging;

import java.net.Socket;

import messaging.messages.Message;
import encryption.KeyPair;

/**
 ** This is the class of Plane. It is responsible for create a array of the planes
 * in the Tour to save all the information of the planes which have communicated
 * with the Tour. It has several parameters.
 * 
 ** @param planeID
 ** @param keypair
 *            save the necessary Keypair for encrypted communication.
 ** @param messages
 *            The table of the messages that have been given and send.!! Not
 *            used yet for I can't find a way to initialize the abstract message
 ** @author Hantao Zhao
 ** @author Frederic Jacobs
 **/
public class Plane {

	private String planeID = "";
	private KeyPair keypair;
	private boolean mayDay = false ;
	private Message messages[];
	private int messageNo = 0;
	private static int posX;
	private static int posY;
	private Socket socket = null;
	private String planeType = null;
	private long initialTime;
	private long landingTimeTotal;
	private double consommation;
	private int passager;

	public String getPlaneType() {
		return planeType;
	}

	public void setPlaneType(String planeType) {
		this.planeType = planeType;
		switch (this.planeType) {
		case "A320;":
			this.consommation = 60;
			this.passager = 179;
			break;
		case "A380;":
			this.consommation = 115;
			this.passager = 644;
			break;
		case "B787---DREAMLINER;":
			this.consommation = 63;
			this.passager = 242;
			break;
		case "CONCORDE;":
			this.consommation = 461;
			this.passager = 140;
			break;
		case "GRIPEN;":
			this.consommation = 200;
			this.passager = 1;
			break;
		}
	}

	public long getlandingTimeTotal() {
		return landingTimeTotal;
	}

	public void setlandingTimeTotal(long landingTimeTotal) {
		this.landingTimeTotal = landingTimeTotal;
	}

	public Plane() {
	}

	/**
	 * The setter of the message, add one message to the array
	 * 
	 * @param mes
	 *            The message that should be saved
	 * @return void
	 **/

	public void addMessage(Message mes) { // Unfinished , cant be used, We can't
											// initialize the abstract message
		messages[messageNo] = mes;
		messageNo++;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * The getter of the message
	 * 
	 * @param mes
	 *            The message that should be saved
	 * @return void
	 **/
	public Message[] getMessage() {
		return messages;
	}

	/**
	 * The setter and getter of the PlaneID
	 * 
	 * @param PlaneID
	 *            The PlaneID that should be saved
	 */
	public void setPlaneID(String planeID) {
		this.planeID = planeID;
		setPlaneType(planeID);
	}

	public String getPlaneID() {
		return planeID;
	}

	/**
	 * The setter and getter of the KeyPair
	 * 
	 * @param keyPair
	 *            The KeyPairthat should be saved
	 */
	public void setKeypair(KeyPair keypair) {
		this.keypair = keypair;
	}

	public KeyPair getKeypair() {
		return keypair;
	}

	/**
	 * The setter and getter of the positions , X and Y. TBD: should we change
	 * the posx and posy into a table
	 * 
	 * @param keyPair
	 *            The KeyPairthat should be saved
	 */
	public void setPosx(int posx) {
		Plane.posX = posx;
	}

	public void setPosy(int posy) {
		Plane.posY = posy;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public boolean hasCrashed() {
		return false;
	}

	public long getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(long initialTime) {
		this.initialTime = initialTime;
	}

	public double getConsommation() {
		return consommation;
	}

	public void setConsommation(double consommation) {
		this.consommation = consommation;
	}

	public int getPassager() {
		return passager;
	}

	public void setPassager(int passager) {
		this.passager = passager;
	}
	
	public boolean getMayDayStatus (){
		return mayDay;
	}
	
	public void setMayDayStatus(boolean mayDayStatus){
		mayDay = mayDayStatus;
	}
	
}
