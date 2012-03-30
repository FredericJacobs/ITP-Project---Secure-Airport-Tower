package messaging;

import messaging.messages.Message;

import encryption.KeyPair;

public class Plane {

	private String planeID = "";
	private KeyPair keypair;
	private Message messages[];
	private int messageNo = 0;
	private int posX;
	private int posY;

	public Plane(String planeID, KeyPair keypair) {
		super();
		this.planeID = planeID;
		this.keypair = keypair;
	}

	public void addMessage(Message mes) {
		messages[messageNo] = mes;
		messageNo++;
	}

	public void setPlaneID(String planeID) {
		this.planeID = planeID;
	}

	public void setKeypair(KeyPair keypair) {
		this.keypair = keypair;
	}

	public void setMessage(Message[] message) {
		this.messages = message;
	}

	public String getPlaneID() {
		return planeID;
	}

	public KeyPair getKeypair() {
		return keypair;
	}

	public Message[] getMessage() {
		return messages;
	}

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
