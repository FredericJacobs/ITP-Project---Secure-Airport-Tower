package messaging.messages;

public class DataMessage extends Message {
	public static final int MAX_PACKET_SIZE = 1024; 
	byte[] hash; // 20 octets (20 bytes) 
	int continuation;
	byte[] format; // 4 octets (4 bytes) 
	int fileSize;
	byte[] payload;
	
	public DataMessage (byte[] planeID, int continuation, int posx,
			int posy, byte [] hash, byte[] format, int fileSize, byte[] payload) {
		super (planeID, payload.length, 4, posx, posy, MessageType.DATA);
		this.continuation = continuation;
		this.hash = hash ;
		this.format = format ; 
		this.fileSize = fileSize ; 
		this.payload = payload; 
	}
	public byte[]  getPayload() {
		return this.payload;
	}
}
