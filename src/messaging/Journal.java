package messaging;

import java.util.ArrayList;

public class Journal {
	public Journal(){}
	public static ArrayList<Event> list = new ArrayList<Event>();

	public void addEvent(Event e){
		list.add(e);
	}
	
	public void getEvent(int i){
		list.get(i);
	}
	public int listSize(){
		return list.size();
	}
	
}
