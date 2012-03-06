package DataFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.io.RandomAccessFile;

public class DataFile extends File {

	protected static int sizOfLastpack; 

	protected boolean finished = false;

	public DataFile(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */

	public static void main(String args[]) throws Exception {
		
		String directory = "F:\\Java\\Project\\ITP-Project---Secure-Airport-Tower";
		String name = "README.md";
		File f = new File(directory, name);
		RandomAccessFile file = null;
		Byte[] hash = new Byte[1024];
	//	Byte[][] received = new Byte[1000000][1024];
		int sizOfLastpack; 

		try {
			file = new RandomAccessFile(f, "rw");
			byte[] b = new byte[4];
			sizOfLastpack = (int) (file.length()%1024);
			try {
				long len = file.length();
				//file.read(b); 
			//	file.seek(1);
				//for (int n = 0; n < file.length()/1024;n++){
				System.out.println(len);
				for (int i = 0; i < 1024; i++) {
					hash[i] = file.readByte();
					System.out.println(hash[i]);
				}
			//	received[n] = hash;
				hash =null;
			//	}
			 }catch (IOException e) {
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
}
