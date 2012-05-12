package Plane;

import java.io.DataOutputStream;
import java.io.IOException;
import DataFile.DataFile;

import messaging.messages.DataMessage;
import messaging.messages.HelloMessage;
import messaging.messages.Message;

/**
 * This class help the Plane to handle different type of messages
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class PlaneMessageHandler {
	
	DataFile towerDataFile = null;
	
	public PlaneMessageHandler() {
	}

	/**
	 * The respond method,it will respond a message and output the necessary
	 * information by sending it into the DataOutputStream
	 * 
	 * @param plane
	 *            The corresponded plane
	 * @param planenumber
	 * @param message
	 *            The message that need to be handled
	 * @param outData
	 *            The DataOutputStream where we send the feed back message
	 * @throws IOException
	 */
	public void respond(Message message,
			DataOutputStream outData) throws IOException {
		int type = message.getType();
		
		switch (type) {
		
		case 1:
			if (towerDataFile == null){
				towerDataFile = new DataFile ("testfile", (DataMessage) message);
			}

		case 2:
			
		
		default:
		}
	}

}
