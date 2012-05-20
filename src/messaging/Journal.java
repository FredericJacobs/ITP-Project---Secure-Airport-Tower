package messaging;

import GUI.AirportGUI;

import java.util.ArrayList;
import java.util.Observable;

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
		setChanged();
		notifyObservers(getList());
		boolean updatedPosition = false;
		// Find Plane in Array and add it's position
		for (int i = 0; i < positions.size(); i++) {

			//System.out.println(positions.get(i).getPlaneID());
			//System.out.println(e.getSource());

			if (e.getSource().equalsIgnoreCase(positions.get(i).getPlaneID())) {
				positions.get(i).updatePosition(e.getMessage().getPosition());
				updatedPosition = true;
				if (e.getMessage().getType() == 6){
					positions.get(i).setMayDayStatus(true);
				}
			//	System.out.println("position updated");
				break;
			}

		}
		if (!updatedPosition) {
			positions.add(new PlanePosition(e.getSource(), e.getMessage()
					.getPosition()));
		//	System.out.println("Plane ID added");

		}
	}

	public Event getEvent(int i) {
		return getList().remove(0);
	}

	public int listSize() {
		return getList().size();
	}

	public static ArrayList<Event> getList() {
		return list;
	}
	
	public void planeHasCrashed(String planeID){
		for (int i=0; i < positions.size(); i++){
			if (positions.get(i).getPlaneID().equalsIgnoreCase(planeID)){
				positions.get(i).didCrash(true);
			}
		}
	}

	public static void setList(ArrayList<Event> list) {
		Journal.list = list;
	}

	public void planeDidLand(String planeID) {
		for (int i=0; i < positions.size(); i++){
			if (positions.get(i).getPlaneID().equalsIgnoreCase(planeID)){
				positions.remove(i);
			}
		}
	}

}
