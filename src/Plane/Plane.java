package Plane;

import java.io.BufferedReader;

import messaging.ReadMessages;
import messaging.messages.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Plane {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String planeID = "B1778000";
		DataOutputStream outData = null;
		DataInputStream inData = null;
		// Begin to connect by the net work socket , using the port "LOCALHOST",
		// 6900
		try {
			kkSocket = new Socket("LOCALHOST", 6900);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));

			outData = new DataOutputStream(kkSocket.getOutputStream());
			inData = new DataInputStream(kkSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: LOCALHOST.");
			System.exit(1);
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to: LOCALHOST.");
			System.exit(1);
		}
		// begin to transfer messages
		System.out.println("This is plane B1778000 please give instructions");
		System.out
				.println("0=HELLO, 1=DATA, 2=MAYDAY, 3=SENDRSA, 4=CHOKE, 5=UNCHOKE, 6=BYE,7=ROUTING, 8=KEEPALIVE, 9=LANDINGREQUEST");
		Scanner scanner = new Scanner(System.in);

		int i = 0;
		while (i != 6) {
			i = scanner.nextInt();
			switch (i) {
			case 0:
				HelloMessage hello = new HelloMessage(planeID.getBytes(), 20,
						10, (byte) 0);
				hello.write(outData);
				System.out.println("----Messages from the tour-----");
				ReadMessages.readMessage(inData).print();
				break;
			case 6:
				ByeMessage bye = new ByeMessage(planeID.getBytes(),0, 20, 10);
				bye.write(outData);
				System.out.println("----Messages from the tour-----");
				ReadMessages.readMessage(inData).print();
				break;
			case 8:
				KeepAliveMessage KeepAlive = new KeepAliveMessage(
						planeID.getBytes(), 20, 10, (byte) 0);
				KeepAlive.write(outData);
				System.out.println("----Messages from the tour-----");break;
			}
		}
		out.close();
		in.close();
		kkSocket.close();
	}
}