package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class RoutingMessage extends Message {
	public enum routingMessageType {
		NEWFIRST, LAST, REPLACEALL;
		public static routingMessageType routingMessageTypeName(int i) {
			routingMessageType TypeR = null;
			switch (i) {
			case 0:
				TypeR = NEWFIRST;
			case 1:
				TypeR = LAST;
			case 2:
				TypeR = REPLACEALL;
			}
			return TypeR;
		}
	}

	public enum moveType {
		STRAIGHT, CIRCULAR, LANDING, NONE, DESTRUCTION;
		public static moveType moveMessageTypeName(int i) {
			moveType TypeM = null;
			switch (i) {
			case 0:
				TypeM = STRAIGHT;
			case 1:
				TypeM = CIRCULAR;
			case 2:
				TypeM = LANDING;
			case 3:
				TypeM = NONE;
			case 4:
				TypeM = DESTRUCTION;
			}
			return TypeM;
		}
	}

	routingMessageType TypeR;
	moveType TypeM;
	byte[] payload;

	public RoutingMessage(byte[] planeID, int length, int posx, int posy,
			routingMessageType typeR, moveType typeM, byte[] payload) {
		super(planeID, length, 2, posx, posy, MessageType.ROUTING);
		TypeR = typeR;
		TypeM = typeM;
		this.payload = payload;
	}

	public void write(DataOutputStream out) throws IOException {
		super.write(out);
		out.writeInt(TypeR.ordinal());
		out.writeInt(TypeM.ordinal());
		out.write(payload);
	}

}