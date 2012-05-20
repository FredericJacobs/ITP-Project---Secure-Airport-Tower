package messaging;

import java.util.*;
import messaging.messages.Message;

public class Event extends Vector<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}