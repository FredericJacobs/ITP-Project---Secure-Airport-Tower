package messaging;

import java.util.Date;
import java.util.Vector;

import messaging.messages.Message;

/**
 * This class is a part of the journal. Each time a message is created or send
 * out, it will be saved as an Event . It contain the information such as
 * message, source, destination and the time at which it is created.
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs * @version 1.0
 */
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