package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class MayDayMessage extends Message {
	private String cause;

	public MayDayMessage(byte[] planeID, int length, int posx, int posy,
			String cause) {
		super(planeID, length, 0, posx, posy, MessageType.MAYDAY);
		this.cause = cause;
	}

	public String getCause() {
		return cause;
	}
	/** Getter to see if the hello message requires Crypted communication or not
	 * @return boolean: true for Crypted and false for not Crypted
	**/
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
		out.writeInt(length);
		out.write(cause.getBytes());
	}

}