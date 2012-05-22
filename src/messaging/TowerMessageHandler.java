package messaging;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

import dataFile.DataFile;
import GUI.AirportGUI;
import messaging.messages.*;
import messaging.messages.RoutingMessage.moveType;
import messaging.messages.RoutingMessage.routingMessageType;

/**
 * This class help the Tour to handle different type of messages
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 */
public class TowerMessageHandler extends Observable {

	DataFile towerDataFile = null;
	int fileCount = 0;
	int continuation = 0;
	DataMessage[] messages = new DataMessage[10];
	int[] numberForHash = new int[10];
	ArrayList<File> listOfDownloads = new ArrayList<File>();

	public TowerMessageHandler() {
		addObserver(AirportGUI.getModesGUI());
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
	public int respond(Plane plane, Message message,
			DataOutputStream outData) throws IOException {
		int type = message.getType();
		Event event = new Event(message, message.getPlaneID(), "Tower");
		Tower.getInstance().getJournal().addEvent(event);
		plane.setPlaneID(message.getPlaneID());
		// We answer most the messages by implementing the visitor pattern. While the Datafile and the bye message
		// need some of the special parameter so we still keep the switch function for them.
		Visitor visitor = new Visitor();
		 
		switch (type) {// depends on different type of message we go to
						// different cases
		// Answer the Data message 
		case 1:

			towerDataFile = new DataFile("downloads" + File.separator
					+ plane.getPlaneID() + "-" + fileCount,
					(DataMessage) message);
			if (towerDataFile.isComplete()) {
				fileCount++;
				listOfDownloads.add(towerDataFile);
				AirportGUI.updateDownloads(listOfDownloads);
				// Read the plane type and save it in the plane class.
				try {
					Scanner scanner = new Scanner(new FileInputStream("downloads"+ File.separator + plane.getPlaneID()+"-0.txt"));
					
					String delimiters = "[=]"; 
					String[ ] tokens = scanner.nextLine().split(delimiters);
					plane.setPlaneID(tokens[1]);					
				} catch (FileNotFoundException e) {
					System.out.println("File was not found");
				}
			}

			return 0;
		// Answer the Bye message 
		case 6:
			changeCircle();
			System.out.println("Connection terminated");
			plane.setlandingTimeTotal(System.currentTimeMillis()
					- plane.getInitialTime());
			Tower.passgerNumber += plane.getPassager();
			Tower.landingTimeTotal += plane.getlandingTimeTotal();
			// Read the fuel consumption and save it in the plane class and update it in the modesGUI.
			try {
				Scanner scanner = new Scanner(new FileInputStream("downloads"+ File.separator + plane.getPlaneID()+"-1.txt"));
				String delimiters = "[=]"; 
				String[ ] tokens = scanner.nextLine().split(delimiters);
				String delimiters2 = "[;]"; 
				String[ ] tokens2 = tokens[1].split(delimiters2);
				int consumptionPlane =  Integer.valueOf(tokens2[0]).intValue(); 
				Tower.consumption += consumptionPlane;
			} catch (FileNotFoundException e) {
				System.out.println("File was not found");
			}		
			Tower.getInstance().getPlanes().remove(plane);
			setChanged();
			notifyObservers();
			return 0;
		default:
			// All the other messages will be handled by the visitor pattern
			return message.accept(visitor, plane, outData);
		}
	}

	/* This method helps the bye message to arrange the other planes. Each time a plane has landed and the landing
	 route is valid again, the changeCircle will call the next plane to come to land.Then it will check if there
	is other planes are waiting;if so the waiting planes will be instructed to enter the smaller circle so that 
	in this way we can arrange all the landing request well.
	*/
	public void changeCircle() {
		Tower.landingRoute.remove(0);
		// Check if there is other planes in the small circle, if so let them enter the landing route
		if (Tower.smallCircle.size() != 0) {
			Plane planeSmall = Tower.smallCircle.remove(0);
			Tower.landingRoute.add(planeSmall);
			try {
				DataOutputStream outData = new DataOutputStream(planeSmall
						.getSocket().getOutputStream());
				RoutingMessage respondLanding0 = new RoutingMessage(
						"Tour0000".getBytes(), 420, 166,
						routingMessageType.REPLACEALL, moveType.STRAIGHT,
						Circle.int2bytes(0));
				respondLanding0.write(outData);
				RoutingMessage respondLanding = new RoutingMessage(
						"Tour0000".getBytes(), Tower.landingPointX,
						Tower.landingPointY, routingMessageType.LAST,
						moveType.LANDING, Circle.int2bytes(0));
				respondLanding.write(outData);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Check if there is other planes in the middle circle, if so let them enter the small circle
			if (Tower.middleCircle.size() != 0) {
				Plane planeMiddle = Tower.middleCircle.remove(0);
				Tower.smallCircle.add(planeMiddle);
				try {
					DataOutputStream outData = new DataOutputStream(planeMiddle
							.getSocket().getOutputStream());
					RoutingMessage respondLanding0 = new RoutingMessage(
							"Tour0000".getBytes(), 400, 150,
							routingMessageType.REPLACEALL, moveType.STRAIGHT,
							Circle.int2bytes(0));
					respondLanding0.write(outData);
					RoutingMessage respondLanding1 = new RoutingMessage(
							"Tour0000".getBytes(), Tower.smallPointX,
							Tower.smallPointY, routingMessageType.LAST,
							moveType.CIRCULAR,
							Circle.int2bytes(Tower.middleAngle));
					respondLanding1.write(outData);

				} catch (IOException e) {
					e.printStackTrace();
				}
				// Check if there is other planes in the long circle, if so let them enter the middle circle
				if (Tower.longCircle.size() != 0) {
					Plane planeLong = Tower.longCircle.remove(0);
					Tower.middleCircle.add(planeLong);

					try {
						DataOutputStream outData = new DataOutputStream(
								planeLong.getSocket().getOutputStream());
						RoutingMessage respondLanding0 = new RoutingMessage(
								"Tour0000".getBytes(), 300, 650,
								routingMessageType.REPLACEALL,
								moveType.STRAIGHT, Circle.int2bytes(0));
						respondLanding0.write(outData);
						RoutingMessage respondLanding1 = new RoutingMessage(
								"Tour0000".getBytes(), Tower.middlePointX,
								Tower.middlePointY, routingMessageType.LAST,
								moveType.CIRCULAR,
								Circle.int2bytes(Tower.middleAngle));
						respondLanding1.write(outData);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
}