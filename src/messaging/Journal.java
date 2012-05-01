package messaging;

import GUI.AirportGUI;

import java.util.ArrayList;
import java.util.Observable;

public class Journal extends Observable {
	public Journal() {
		addObserver(AirportGUI.getJournalGUI());
	}

	public static ArrayList<Event> list = new ArrayList<Event>();
	public ArrayList<PlanePosition> positions = new ArrayList<PlanePosition>();

	public void addEvent(Event e) {
		list.add(e);
		setChanged();
		notifyObservers(list);
		boolean updatedPosition = false;
		// Find Plane in Array and add it's position
		for (int i = 0; i < positions.size(); i++) {

			System.out.println(positions.get(i).getPlaneID());
			System.out.println(e.getSource());

			if (e.getSource().equalsIgnoreCase(positions.get(i).getPlaneID())) {
				positions.get(i).updatePosition(e.getMessage().getPosition());
				updatedPosition = true;
				System.out.println("position updated");
				break;
			}
		}
		if (!updatedPosition) {
			positions.add(new PlanePosition(e.getSource(), e.getMessage()
					.getPosition()));
			System.out.println("Plane ID added");

		}
	}

	public Event getEvent(int i) {
		return list.remove(0);
	}

	public int listSize() {
		return list.size();
	}

}
