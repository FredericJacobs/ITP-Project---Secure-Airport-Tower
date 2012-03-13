package messaging.messages;

public class MayDayMessage extends Message {
	private String cause;
	
	public MayDayMessage (byte[] planeID, int length, int posx,
			int posy, String cause) {
		super (planeID, length, 0, posx, posy, MessageType.MAYDAY);
		this.cause = cause;
	}


	public String getCause (){
		return cause;
	}

}