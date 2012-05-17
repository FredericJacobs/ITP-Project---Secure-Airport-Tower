package messaging;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.FileHandler;

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
		switch (type) {// depends on different type of message we go to
						// different cases
		case 0:
			if (((HelloMessage) message).isCrypted()) {// To see if the hello is
														// crypted or not, then
														// give different
														// respond hello message
				HelloMessage respondHelloMessage = new HelloMessage(
						"Tour0000".getBytes(), 0, 0, (byte) (1 << 4));
				respondHelloMessage.write(outData);
				Event eventR = new Event(respondHelloMessage, "Tower",
						message.getPlaneID());
				Tower.journal.addEvent(eventR);
				plane.setInitialTime(System.currentTimeMillis());
				return 1;
			}

			else {
				HelloMessage respondHelloMessage = new HelloMessage(
						"Tour0000".getBytes(), 0, 0, (byte) 0);
				plane.setPlaneID(message.getPlaneID());
				respondHelloMessage.write(outData);
				Event eventR = new Event(respondHelloMessage, "Tower",
						message.getPlaneID());
				Tower.journal.addEvent(eventR);
				plane.setInitialTime(System.currentTimeMillis());
				return 0;
			}

		case 1:

			towerDataFile = new DataFile("downloads" + File.separator
					+ plane.getPlaneID() + "-" + fileCount,
					(DataMessage) message);
			if (towerDataFile.isComplete()) {
				fileCount++;
				listOfDownloads.add(towerDataFile);
				AirportGUI.updateDownloads(listOfDownloads);
				try {
					Scanner scanner = new Scanner(new FileInputStream("F:\\Java\\NewProject\\ITP-Project---Secure-Airport-Tower\\downloads\\"+plane.getPlaneID()+"-0.txt"));
					
					String delimiters = "[=]"; 
					String[ ] tokens = scanner.nextLine().split(delimiters);
					plane.setPlaneID(tokens[1]);					
				} catch (FileNotFoundException e) {
					System.out.println("File was not found");
				}
				// plane.setPlaneType();
			}

			return 0;

		case 2:
			System.out.println("Try to handle the mayday message");
			AirportGUI.choker.chokeEnabled(true);
			Circle.landingUrgent(plane, outData);
			return 0;// Mayday
		case 3:// SendRSA, unfinished for the keypair
			// Tower.planes[planenumber].setKeypair(message.getPublicKey());
			return 2;
			// case 4,5,7 shouldnt happen to the tour
		case 6:
			changeCircle();
	
			System.out.println("Connection terminated");
			plane.setlandingTimeTotal(System.currentTimeMillis()
					- plane.getInitialTime());
			Tower.passgerNumber += plane.getPassager();
			setChanged();
			notifyObservers(Tower.passgerNumber);
			try {
				Scanner scanner = new Scanner(new FileInputStream("F:\\Java\\NewProject\\ITP-Project---Secure-Airport-Tower\\downloads\\"+plane.getPlaneID()+"-1.txt"));
				String delimiters = "[=]"; 
				String[ ] tokens = scanner.nextLine().split(delimiters);
				String delimiters2 = "[;]"; 
				String[ ] tokens2 = tokens[1].split(delimiters2);
				int consumptionPlane =  Integer.valueOf(tokens2[0]).intValue(); 
				Tower.consumption += consumptionPlane;
			} catch (FileNotFoundException e) {
				System.out.println("File was not found");
			}		
			Tower.planes.remove(plane);
			return 0;
		case 7:
			Tower.landingRoute.remove(0);
			ByeMessage respondHelloMessage = new ByeMessage(
					"Tour0000".getBytes(), 0, 0, 0);
			respondHelloMessage.write(outData);
		case 8:
			plane.setPosx(((KeepAliveMessage) message).keepaliveX());
			plane.setPosy(((KeepAliveMessage) message).keepaliveY());
			return 0;
		case 9:// Landing request
				// Handle the landing message
			Circle.answerLandingRequest(plane, outData);
			return 0;
		default:
			return 0;
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