package plane;

import java.util.ArrayList;

public class RoadInstructionsList {
	
	static private ArrayList<RoutingInstruction> roadInstructions = new ArrayList<>();
	
	public RoadInstructionsList(){
		
	}
	
	public static void addInstruction (RoutingInstruction instruction){
		roadInstructions.add(instruction);
	}
	
	public static RoutingInstruction nextInstruction (){
		roadInstructions.remove(0);
		return roadInstructions.get(0);
	}
	
	public static RoutingInstruction getCurrent (){
		return roadInstructions.get(0);
	}
	
	public static void replaceAll (RoutingInstruction instruction){
		roadInstructions.clear();
		addInstruction(instruction);
	}
	
	

}
