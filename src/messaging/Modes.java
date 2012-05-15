package messaging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class Modes {

	@SuppressWarnings("unchecked")
	public void reOrganise(Comparator comparator) {
		Collections.sort(Tower.planes, comparator);
		for (int i = 0; i < Tower.planes.size(); i++) {
			Plane plane = Tower.planes.get(i);
			DataOutputStream outData;
			try {
				outData = new DataOutputStream(plane.getSocket()
						.getOutputStream());
				Circle.answerLandingRequest(plane, outData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (Tower.smallCircle.size() != 0) {

		}

	}
}

class Chronos {

}

class Fuel {
	Comparator<Plane> comparatorFuel = new Comparator<Plane>() {
		public int compare(Plane p1, Plane p2) {
			if (p1.getConsommation() != p2.getConsommation()) {
				return (int) (p1.getConsommation() - p2.getConsommation());
			} else {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			}
		}

	};
}

// Use Collections.sort(List,comparator) to make the array happen
class Time {

	Comparator<Plane> comparatorTime = new Comparator<Plane>() {
		public int compare(Plane p1, Plane p2) {
			if (p1.getPassager() != p2.getPassager()) {
				return (int) (p1.getPassager() - p2.getPassager());
			} else {
				return (int) (p1.getInitialTime() - p2.getInitialTime());
			}
		}

	};

}
