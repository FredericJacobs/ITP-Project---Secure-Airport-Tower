package messaging.messages;

public class MayDayMessage extends Message {
	private String cause;
	
	public MayDayMessage (byte[] planeID, int length, int priority, int posx,
			int posy, String cause) {
		super (planeID, length, priority, posx, posy, MessageType.MAYDAY);
		this.cause = cause;
	}


	public String getCause (){
		return cause;
	}

}