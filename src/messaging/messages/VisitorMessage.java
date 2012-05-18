package messaging.messages;

import messaging.Visitor;

public interface VisitorMessage {
	public void accept(Visitor visitor);
}
