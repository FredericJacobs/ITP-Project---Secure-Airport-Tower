package Plane;

public enum PlaneType {
	A320(0.78, 10000, 60, 179), 
	A380(0.89, 80000, 115, 644), 
	B787(0.85, 15000, 63, 242), 
	CONCORDE(2.02, 120000, 461, 140), 
	GRIPEN(2, 45000, 200, 1), 
	CUSTOM;

	private double machSpeed;
	private int fuelCapacity;
	private int consumption;
	private int passagerCapacity;
	
	PlaneType (double machSpeed, int fuelCapacity, int consumption, int passengerCapacity){
		this.machSpeed = machSpeed;
		this.fuelCapacity = fuelCapacity;
		this.consumption = consumption;
		this.passagerCapacity = passengerCapacity;
	}
	
	
}
