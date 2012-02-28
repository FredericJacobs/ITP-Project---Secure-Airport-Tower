package Tour;

import java.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import Message.*;

public class Tour

{

	private static Tour instance;

	private Tour() {
		System.out.println("Tour created");
	}

	public static Tour getInstance() {
		if (instance == null)
			instance = new Tour();
		return instance;
	}
}
