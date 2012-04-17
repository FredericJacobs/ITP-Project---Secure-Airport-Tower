package messaging;

import java.util.*;
import messaging.messages.Message;

public class Event extends Vector{
	private Message message;
	private String source;
	private String destination;
	private Date date;
	public Event(Message message, String source, String destination) {
		super();
		this.message = message;
		this.source = source;
		this.destination = destination;
		date = new Date();
	}
}