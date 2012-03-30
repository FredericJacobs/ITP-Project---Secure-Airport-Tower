package main;

import twitter.*;
import GUI.*;

/**
 ** This is a class for us to try the function of Twitter. We may want to combien it with the others in the future modifications
 **/
public class MainClass {

	public static void main(String[] args) {
		new Radar(args.length == 0 ? null : args[0]).setVisible(true); 
	
	}

}
