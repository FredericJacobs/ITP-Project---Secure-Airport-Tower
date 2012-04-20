package messaging;

import java.util.*;
import messaging.messages.Message;

@SuppressWarnings({ "rawtypes", "serial" })
public class Event extends Vector{
	@SuppressWarnings("unused")
	private Message message;
	@SuppressWarnings("unused")
	private String source;
	@SuppressWarnings("unused")
	private String destination;
	@SuppressWarnings("unused")
	private Date date;
	public Event(Message message, String source, String destination) {
		super();
		this.message = message;
		this.source = source;
		this.destination = destination;
		date = new Date();
	}
}