package Plane;

import generals.XYPosition;

public class PlaneNavigation implements Runnable {
	
	double planeSpeed = Plane.getPlaneSpeed();

	@Override
	public void run() {
		while (true){
			
			this.updatePosition();
			
			// Pause this thread until the next positon update
			try {
				Thread.sleep(TestPlane.getPlaneUpdateInterval());
			} catch (InterruptedException e) {
				
			}
		}
		
	}

	private void updatePosition() {
		
		XYPosition currentPosition = Plane.getPosition();
		
		
		
		
	}

}
