package messaging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

/*This class is the main part of the ModesGUI, it contains two kind of 
 * 
 */
public class Modes {

	// Three kind of comparator to match different modes
	public static Comparator<Plane> comparatorChronos = new Comparator<Plane>() {
		public int compare(Plane p1, Plane p2) {
			if (p1.getInitialTime() != p2.getInitialTime()) {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			} else {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			}
		}

	};
	public static Comparator<Plane> comparatorFuel = new Comparator<Plane>() {
		public int compare(Plane p1, Plane p2) {
			if (p1.getConsommation() != p2.getConsommation()) {
				return (int) (p1.getConsommation() - p2.getConsommation());
			} else {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			}
		}

	};

	public static Comparator<Plane> comparatorTime = new Comparator<Plane>() {
		public int compare(Plane p1, Plane p2) {
			if (p1.getPassager() != p2.getPassager()) {
				return (int) (p1.getPassager() - p2.getPassager());
			} else {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			}
		}

	};
	//The action when the button CHRONOS is clicked 

	public static void reOrganiseChronos() {
		Collections.sort(Tower.planes, comparatorChronos);
		Tower.landingRoute.clear();
		Tower.smallCircle.clear();
		Tower.middleCircle.clear();
		Tower.longCircle.clear();
		for (int i = 0; i < Tower.planes.size(); i++) {
			Plane plane = Tower.planes.get(i);
			DataOutputStream outData;
			try {
				outData = new DataOutputStream(plane.getSocket()
						.getOutputStream());
				Circle.answerLandingRequest(plane, outData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//The action when the button FUEL is clicked 
	public static void reOrganiseFuel() {
		Tower.landingRoute.clear();
		Tower.smallCircle.clear();
		Tower.middleCircle.clear();
		Tower.longCircle.clear();
		Collections.sort(Tower.planes, comparatorFuel);
		for (int i = 0; i < Tower.planes.size(); i++) {
			Plane plane = Tower.planes.get(i);
			DataOutputStream outData;
			try {
				outData = new DataOutputStream(plane.getSocket()
						.getOutputStream());
				Circle.answerLandingRequest(plane, outData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//The action when the button TIME is clicked 
	public static void reOrganiseTime() {
		Tower.landingRoute.clear();
		Tower.smallCircle.clear();
		Tower.middleCircle.clear();
		Tower.longCircle.clear();
		Collections.sort(Tower.planes, comparatorTime);
		for (int i = 0; i < Tower.planes.size(); i++) {
			Plane plane = Tower.planes.get(i);
			DataOutputStream outData;
			try {
				outData = new DataOutputStream(plane.getSocket()
						.getOutputStream());
				Circle.answerLandingRequest(plane, outData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
