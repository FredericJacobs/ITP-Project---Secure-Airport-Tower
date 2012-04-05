package messaging;

import java.util.Vector;

import javax.xml.crypto.Data;

public class Events extends Vector {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int priority;
	private String type;
	private String source;
	private String destination;
	private Data data;

	public Events(int priority, String type, String source, String destination) {
		super();
		this.priority = priority;
		this.type = type;
		this.source = source;
		this.destination = destination;
	}
}