package messaging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class is the main part of the ModesGUI, it contains three kinds of
 * Comparator to rank the ArrayList of the tower
 * 
 * The comparatorChronos is based on the time order of the plane so we judge it
 * by comparing the initial time of the planes. The comparatorFuel is based on
 * the consommation of the plane so we judge it by comparing the consummation of
 * the planes. The comparatorTime is based on the waiting time of the plane so
 * we judge it by comparing the passage number of the planes.
 * 
 * If the comparators are the same then we will arrange them by using the
 * initial time of the planes.
 * 
 * @author Hantao Zhao
 * @author Frederic Jacobs
 * @version 1.0
 **/
public class Modes {

	// Three kind of comparator to match different modes
	public static Comparator<Plane> comparatorChronos = new Comparator<Plane>() {
		@Override
		public int compare(Plane p1, Plane p2) {
			if (p1.getInitialTime() != p2.getInitialTime()) {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			} else {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			}
		}

	};
	public static Comparator<Plane> comparatorFuel = new Comparator<Plane>() {
		@Override
		public int compare(Plane p1, Plane p2) {
			if (p1.getConsommation() != p2.getConsommation()) {
				return (int) (p1.getConsommation() - p2.getConsommation());
			} else {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			}
		}

	};

	public static Comparator<Plane> comparatorTime = new Comparator<Plane>() {
		@Override
		public int compare(Plane p1, Plane p2) {
			if (p1.getPassager() != p2.getPassager()) {
				return p1.getPassager() - p2.getPassager();
			} else {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			}
		}

	};

	// The action when the button CHRONOS is clicked

	public static void reOrganiseChronos() {
		// Clear the current plane arrays
		Tower.getInstance().getLandingRoute().clear();
		Tower.getInstance().getSmallCircle().clear();
		Tower.getInstance().getMiddleCircle().clear();
		Tower.getInstance().getLongCircle().clear();	
		// sort the planes according to the Fuel comparator
		Collections.sort(Tower.getInstance().getPlanes(), comparatorChronos);
		// Send the new instructions to all the planes 
		for (int i = 0; i < Tower.getInstance().getPlanes().size(); i++) {
			Plane plane = Tower.getInstance().getPlanes().get(i);
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

	// The action when the button FUEL is clicked
	public static void reOrganiseFuel() {
		// Clear the current plane arrays
		Tower.getInstance().getLandingRoute().clear();
		Tower.getInstance().getSmallCircle().clear();
		Tower.getInstance().getMiddleCircle().clear();
		Tower.getInstance().getLongCircle().clear();
		// sort the planes according to the Fuel comparator
		Collections.sort(Tower.getInstance().getPlanes(), comparatorFuel);
		// Send the new instructions to all the planes 
		for (int i = 0; i < Tower.getInstance().getPlanes().size(); i++) {
			Plane plane = Tower.getInstance().getPlanes().get(i);
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

	// The action when the button TIME is clicked
	public static void reOrganiseTime() {
		// Clear the current plane arrays
		Tower.getInstance().getLandingRoute().clear();
		Tower.getInstance().getSmallCircle().clear();
		Tower.getInstance().getMiddleCircle().clear();
		Tower.getInstance().getLongCircle().clear();
		// sort the planes according to the Time comparator
		Collections.sort(Tower.getInstance().getPlanes(), comparatorTime);
		// Send the new instructions to all the planes 
		for (int i = 0; i < Tower.getInstance().getPlanes().size(); i++) {
			Plane plane = Tower.getInstance().getPlanes().get(i);
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
