package messaging.messages;

import java.io.DataOutputStream;

import messaging.Plane;
import messaging.Visitor;
/**
 * The interface of the VisitorMessage, all the types of message will implement this interface.
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 *
 */
public interface VisitorMessage {
	public int accept(Visitor visitor,Plane plane,DataOutputStream outData);
}
