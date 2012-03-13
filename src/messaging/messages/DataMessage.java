package messaging.messages;

public class DataMessage extends Message {
	private int fileSize;
	private byte[] hash;
	private int packetNumber;
	private byte[] payload;
	
	public DataMessage (byte[] planeID, int length, int priority, int posx,
			int posy) {
		super (planeID, length, priority, posx, posy, MessageType.DATA);
	}
	public byte[]  getPayload() {
		return this.payload;
	}
}
