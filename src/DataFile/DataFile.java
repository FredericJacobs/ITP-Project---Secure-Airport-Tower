package DataFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.io.RandomAccessFile;

public class DataFile extends File {

	Byte[] hash = new Byte[10000];

	public DataFile(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */

	public static void main(String args[]) throws Exception {
		// RandomAccessFile s = new
		// RandomAccessFile("F://Java//Project//ITP-Project---Secure-Airport-Tower//OutFile.txt",
		// "rw");
		// System.out.println(s.getFilePointer());// 0
		String directory = "F:\\Java\\Project\\ITP-Project---Secure-Airport-Tower";
		String name = "OutFile.txt";
		File f = new File(directory, name);
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(f, "rw");
			byte[] b = new byte[4];
			try {
				long len = file.length();
				file.read(b); // 设置要读取的字节位置
				file.seek(1);
				System.out.println(file.readByte() + ">>FilePointer>>"+ file.getFilePointer());
				for (int i = 0; i < b.length; i++) {
					System.out.println(file.readLine());
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
}
