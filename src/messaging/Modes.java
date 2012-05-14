package messaging;

import java.util.Comparator;

public class Modes {
	
	public void reOrganise(){
		if(Tower.smallCircle.size()!=0){
			
		}
		
	}
}
class Chronos {
	
}
class Fuel{
	Comparator<Plane> comparator = new Comparator<Plane>(){
		public int compare(Plane p1, Plane p2) {
			if(p1.getConsommation()!=p2.getConsommation()){
				return (int) (p1.getConsommation()-p2.getConsommation());
			}else {
				return (int) (p1.getInitialTime()-p2.getInitialTime());
			}
		}
		
	
};}//  Use Collections.sort(List,comparator) to make the array happen
class Time{

	Comparator<Plane> comparator = new Comparator<Plane>(){
		public int compare(Plane p1, Plane p2) {
			if(p1.getPassager()!=p2.getPassager()){
				return (int) (p1.getPassager()-p2.getPassager());
			}else {
				return (int) (p1.getInitialTime()-p2.getInitialTime());
			}
		}
		
	
};
	
}
