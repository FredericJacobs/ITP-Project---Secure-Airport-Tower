package messaging;

import messaging.messages.*;

public class Visitor {
	public void visit(HelloMessage message) {
	}

	public void visit(DataMessage message) {
	}

	public void visit(MayDayMessage message) {
	}

	public void visit(SendRSAMessage message) {
	}

	public void visit(ChokeMessage message) {
	}

	public void visit(UnchokeMessage message) {
	}

	public void visit(ByeMessage message) {
	}

	public void visit(RoutingMessage message) {
	}

	public void visit(KeepAliveMessage message) {
	}

	public void visit(LandingMessage message) {
	}

}
