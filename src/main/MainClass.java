package main;

import twitter.*;
import GUI.*;

public class MainClass {

	public static void main(String[] args) {
		new Radar(args.length == 0 ? null : args[0]).setVisible(true); 
	
	}

}
