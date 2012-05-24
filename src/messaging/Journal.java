package messaging;

import java.util.ArrayList;
import java.util.Observable;

import GUI.AirportGUI;

/**
 * This class is a journal which will record all the event happened during the
 * communication. It will also run the planeHasCrashed or planeDidLand method to
 * help the GUI to print out the needed information. As it is a part of the
 * observer mode, it extends the Observable to remind the journal gui when a new
 * event is saved in.
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 * 
 * 
 */

public class Journal extends Observable {
	public Journal() {
		addObserver(AirportGUI.getJournalGUI());
	}

	private static ArrayList<Event> list = new ArrayList<Event>();
	public static ArrayList<Event> archiveList = new ArrayList<Event>();
	public ArrayList<PlanePosition> positions = new ArrayList<PlanePosition>();

	public synchronized void addEvent(Event e) {

		archiveList.add(e);
		getList().add(e);

		if (e.getSource().equalsIgnoreCase("Tower")) {
			return;
		}

		// Remind the observer that a new event has come in
		setChanged();
		notifyObservers(getList());
		boolean updatedPosition = false;
		// Find Plane in Array and add it's position
		for (int i = 0; i < positions.size(); i++) {

			if (e.getSource().equalsIgnoreCase(positions.get(i).getPlaneID())) {
				positions.get(i).updatePosition(e.getMessage().getPosition());
				updatedPosition = true;
				if (e.getMessage().getType() == 6) {
					positions.get(i).setMayDayStatus(true);
				}
				break;
			}

		}
		if (!updatedPosition) {
			positions.add(new PlanePosition(e.getSource(), e.getMessage()
					.getPosition()));
			// System.out.println("Plane ID added");

		}
	}

	public synchronized Event getEvent(int i) {
		return getList().remove(0);
	}

	public int listSize() {
		return getList().size();
	}

	public static ArrayList<Event> getList() {
		return list;
	}

	// The special event such as plane has crashed
	public void planeHasCrashed(String planeID) {
		for (int i = 0; i < positions.size(); i++) {
			if (positions.get(i).getPlaneID().equalsIgnoreCase(planeID)) {
				positions.get(i).didCrash(true);
			}
		}
	}

	public static void setList(ArrayList<Event> list) {
		Journal.list = list;
	}

	// The special event such as plane has landed
	public void planeDidLand(String planeID) {
		for (int i = 0; i < positions.size(); i++) {
			if (positions.get(i).getPlaneID().equalsIgnoreCase(planeID)) {
				positions.remove(i);
			}
		}
	}
	// The special event such as plane has mayday situation
	public void planeDidSendMayDay(String planeID) {
		for (int i = 0; i < positions.size(); i++) {
			if (positions.get(i).getPlaneID().equalsIgnoreCase(planeID)) {
				positions.get(i).setMayDayStatus(true);
			}
		}
	}

}
