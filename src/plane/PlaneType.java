package plane;

/** This enum defines a few types of planes given in ITP-06 and assigns them their specifications.
 * @author Frederic Jacobs
 * @author Hantao Zhao
 */
public enum PlaneType {
	
	A320(0.78, 10000, 60, 179), A380(0.89, 80000, 115, 644), B787(0.85, 15000,
			63, 242), CONCORDE(2.02, 120000, 461, 140), GRIPEN(2, 45000, 200, 1);

	private double machSpeed;
	private int fuelCapacity;
	private int consumption;
	private int passagerCapacity;

	PlaneType() {
		this.machSpeed = 0.89;
		this.fuelCapacity = 80000;
		this.consumption = 115;
		this.passagerCapacity = 644;
	}

	PlaneType(double machSpeed, int fuelCapacity, int consumption,
			int passengerCapacity) {
		this.machSpeed = machSpeed;
		this.fuelCapacity = fuelCapacity;
		this.consumption = consumption;
		this.passagerCapacity = passengerCapacity;
	}

	public double getMachSpeed() {
		return machSpeed;
	}

	public int getFuelCapacity() {
		return fuelCapacity;
	}

	public int getConsumption() {
		return consumption;
	}

	public int getPassagerCapacity() {
		return passagerCapacity;
	}

}