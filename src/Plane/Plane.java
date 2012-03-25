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
		HelloMessage hello = new HelloMessage(planeID.getBytes(), 20, 10,(byte) 0);
		/*byte[] test = new byte[15];
		test = hello.getByte();
		for (int i = 0; i < test.length; i++) {
		}
		for (int i = 0; i < test.length; i++) {
			System.out.println(test[i]);// it doesnt work to save the planeit
										// into the array
		}
		System.out.println(hello.getByte());*/
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

		/*BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		String fromServer;
		String fromUser;		
		fromServer = in.readLine();
		System.out.println("Server: " + fromServer);
		fromUser = stdIn.readLine();
		out.println(fromUser);	*/		
		hello.write(outData);
		/*while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			if (fromServer.equals("Bye."))
				break;
			fromUser = stdIn.readLine();

			if (fromUser != null) {
				System.out.println("Client: " + fromUser);
				out.println(fromUser);
				hello.write(outData);//try the write method
			//	System.out.println("Return data: " + inData);
			}
		}*/
		System.out.println("----Messages from the tour-----");
		ReadMessages.readMessage(inData).print();
		out.close();
		in.close();
		//stdIn.close();
		kkSocket.close();
	}
}
