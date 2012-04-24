package messaging;

import java.util.ArrayList;

public class Journal {
	public Journal(){}
	public static ArrayList<Event> list = new ArrayList<Event>();
	public ArrayList<PlanePosition> positions = new ArrayList<PlanePosition> ();

	public void addEvent(Event e){
		list.add(e);
		boolean updatedPosition=false;
		// Find Plane in Array and add it's position
		for (int i=0; i<positions.size(); i++){
			
			System.out.println(positions.get(i).getPlaneID());
			System.out.println(e.getSource());
			
			if (e.getSource().equalsIgnoreCase(positions.get(i).getPlaneID())){
				positions.get(i).updatePosition(e.getMessage().getPosition());
				updatedPosition = true;
				System.out.println("position updated");
				break;
			}
		}
		if (!updatedPosition){
			positions.add(new PlanePosition(e.getSource(), e.getMessage().getPosition()));
			System.out.println("Plane ID added");
			
		}	
	}
	
	public void getEvent(int i){
		list.get(i);
	}
	public int listSize(){
		return list.size();
	}
	

}
