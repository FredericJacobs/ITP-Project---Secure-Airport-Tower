package messaging.messages;

import java.io.DataOutputStream;

import messaging.Plane;
import messaging.Visitor;

public interface VisitorMessage {
	public int accept(Visitor visitor,Plane plane,DataOutputStream outData);
}
