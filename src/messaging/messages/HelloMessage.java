package messaging.messages;

public class HelloMessage extends Message {
	private boolean reserved;
	public HelloMessage (byte[] planeID, int length, int priority, int posx,
			int posy, boolean reserved) {
		super (planeID, length, priority, posx, posy, MessageType.HELLO);
		this.reserved = reserved;
	}

	public boolean isCrypted (){
		return reserved;
	}

}
