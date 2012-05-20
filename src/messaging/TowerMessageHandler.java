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
		Tower.journal.addEvent(event);
		plane.setPlaneID(message.getPlaneID());
		Visitor visitor = new Visitor();
		switch (type) {// depends on different type of message we go to
						// different cases
		case 1:

			towerDataFile = new DataFile("downloads" + File.separator
					+ plane.getPlaneID() + "-" + fileCount,
					(DataMessage) message);
			if (towerDataFile.isComplete()) {
				fileCount++;
				listOfDownloads.add(towerDataFile);
				AirportGUI.updateDownloads(listOfDownloads);
				try {
					Scanner scanner = new Scanner(new FileInputStream("downloads"+ File.separator + plane.getPlaneID()+"-0.txt"));
					
					String delimiters = "[=]"; 
					String[ ] tokens = scanner.nextLine().split(delimiters);
					plane.setPlaneID(tokens[1]);					
				} catch (FileNotFoundException e) {
					System.out.println("File was not found");
				}
				// plane.setPlaneType();
			}

			return 0;
		case 6:
			changeCircle();
			System.out.println("Connection terminated");
			plane.setlandingTimeTotal(System.currentTimeMillis()
					- plane.getInitialTime());
			Tower.passgerNumber += plane.getPassager();
			Tower.landingTimeTotal += plane.getlandingTimeTotal();

			try {
				Scanner scanner = new Scanner(new FileInputStream("downloads"+ File.separator + plane.getPlaneID()+"-1.txt"));
				String delimiters = "[=]"; 
				String[ ] tokens = scanner.nextLine().split(delimiters);
				String delimiters2 = "[;]"; 
				String[ ] tokens2 = tokens[1].split(delimiters2);
				int consumptionPlane =  Integer.valueOf(tokens2[0]).intValue(); 
				System.out.println("consumptionPlane" + consumptionPlane);
				Tower.consumption += consumptionPlane;
			} catch (FileNotFoundException e) {
				System.out.println("File was not found");
			}		
			Tower.planes.remove(plane);
			System.out.println("Passage =" + Tower.passgerNumber  + "Time = " + plane.getlandingTimeTotal());
			setChanged();
			notifyObservers();
			return 0;
		default:
			return message.accept(visitor, plane, outData);
		}
	}

	public void changeCircle() {
		Tower.landingRoute.remove(0);
		if (Tower.smallCircle.size() != 0) {
			Plane planeSmall = Tower.smallCircle.remove(0);
			Tower.landingRoute.add(planeSmall);
			try {
				DataOutputStream outData = new DataOutputStream(planeSmall
						.getSocket().getOutputStream());
				RoutingMessage respondLanding = new RoutingMessage(
						"Tour0000".getBytes(), Tower.landingPointX,
						Tower.landingPointY, routingMessageType.REPLACEALL,
						moveType.LANDING, Circle.int2bytes(0));
				respondLanding.write(outData);
			} catch (IOException e) {
				e.printStackTrace();
			}
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