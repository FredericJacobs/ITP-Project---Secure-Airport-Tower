package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class RoutingMessage extends Message {
	public enum routingMessageType {
		NEWFIRST, LAST, REPLACEALL;

		/**
		 * The method to return the routingMessage name by getting the integer
		 * ordinal()
		 * 
		 * @return routingMessageType
		 **/
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
		/**
		 * The method to return the moveType name by getting the integer
		 * ordinal()
		 * 
		 * @return moveType
		 **/
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

	public RoutingMessage(byte[] planeID, int posx, int posy,
			routingMessageType typeR, moveType typeM, byte[] payload) {
		super(planeID, payload.length, 2, posx, posy, MessageType.ROUTING);
		TypeR = typeR;
		TypeM = typeM;
		this.payload = payload;
	}

	/**
	 ** Override of the write message, to send out the supplementary information
	 **/
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
		out.writeInt(TypeR.ordinal());
		out.writeInt(TypeM.ordinal());
		out.write(payload);
	}
	
	

}