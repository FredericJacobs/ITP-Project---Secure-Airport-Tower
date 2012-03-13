package DataFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

import messaging.messages.*;


public class DataFile extends File {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] hash;
	private byte[] payload;
	private static int packetSize;
	private int lastPacketSize;
	private int numberOfPackets;
	
	public DataFile(String path) {
		super(path);
		DataFile.packetSize = 1024;
	}
	
	public byte[] getBlock(int offset) throws IOException, NoSuchAlgorithmException {
		byte[] data = null;
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(this));
		
		stream.read(data, offset, packetSize);
		this.hash = this.computeHash(data);
		
		return data;
	}
	
	public void save(DataMessage msgFile) throws IOException {
		RandomAccessFile file = new RandomAccessFile(this.getCanonicalFile(), "rw");
		file.writeBytes(msgFile.getPayload().toString());
	}
	
	public boolean isComplete() {
		return false;
	}
	
	private byte[] computeHash(byte[] file) throws NoSuchAlgorithmException {
		MessageDigest hasher = MessageDigest.getInstance("SHA-1");
		
		Formatter formatter = new Formatter();
		
		for(byte i : hasher.digest(file)) {
	        formatter.format("%02x", i);
		}
		
		return formatter.toString().getBytes();
	}
}
