package DataFile;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.RandomAccessFile;

import javax.xml.crypto.Data;

public class DataFile extends File {

	public static int sizOfLastpack;

	public boolean finished = false;
	static final int KILOBYTES = 1024;
	public boolean[] checkComplete;

	public DataFile(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main (String args[]) throws IOException{
		divideFile(); // try to divide the file
	}

	// the method to divide the file, always not succeed.....
	//save the all the hashes into the table,shall we save the divided Byte[] into a 
	// Byte[][]? If not how do we send the 1KB file, directly? 
	public static void divideFile() throws IOException {
		String directory = "F:\\Java\\Project\\ITP-Project---Secure-Airport-Tower";
		String name = "README.md";
		File f = new File(directory, name);
		RandomAccessFile file = null;
		Byte[] hash = new Byte[KILOBYTES];
		boolean[] checkComplete;
		Byte[][] divided = new Byte[1000000][KILOBYTES];
		int sizOfLastpack;

		try {
			file = new RandomAccessFile(f, "rw");
			byte[] b = new byte[4];
			sizOfLastpack = (int) (file.length() % KILOBYTES);
			try {
				long len = file.length();
				file.read(b);
				file.seek(1);
				for (int n = 0; n < file.length() / KILOBYTES; n++) {
					System.out.println(len);
					for (int i = 0; i < KILOBYTES; i++) {
						hash[i] = file.readByte();
						System.out.println(hash[i]);
					}
					divided[n] = hash;
					hash = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != file) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// compare if all the Hash file are the same with the original ones ( Very bad trying , almost nothing completed)
	public void hashComparison() throws IOException {

		DataInputStream inputStream = null;
		byte hashCodeData = 0;

		try {
			inputStream = new DataInputStream(new FileInputStream(
					this.getName()));
		}

		catch (IOException e) {
			System.out.println("File not found");
		}

		try {
			while (true) {
				hashCodeData += inputStream.readByte();

			}
		}

		catch (IOException e) {
			inputStream.close();
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		md.update(hashCodeData);
		byte[] hashToCompare = md.digest();
	}

	// Get the data from a message and save it on the disk (not done)
	public void saveOnDisk(Data data) throws IOException {

		RandomAccessFile out = null;
		try {
			out = new RandomAccessFile(this.getName(), "rw");
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	// check if all the package transferring are completed (done)
	public boolean complete(int position) {
		this.checkComplete[position] = true;

		for (int i = 0; i < this.checkComplete.length; i++) {

			if (!this.checkComplete[i])
				return false;
		}

		return true;
	}
}
