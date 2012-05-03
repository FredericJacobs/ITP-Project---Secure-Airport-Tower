package DataFile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import messaging.messages.*;

/**
 * This class is responsible for creating DataMessages objects from a File. It can also write a File from a bunch of DataMessages.
 * The DataMessages should be routed to this DataFile. Order doesn't really matter, except the first one that should be the first message too. 
 * This design pattern isn't by far the best one but we were given this in the ITP Description. We corrected this issue by sorting all the packages again before writing them to the disk.
 * This is clearly not optimized for dealing with huge files.
 * DataFile has two Constructors. One for sending files (creating DataMessages files from a file) and one for receiving files (creating a file from DataMessages).
 * @author Frederic Jacobs
 * @author Hantao Zhao
 * @see DataMessage
 */

public class DataFile extends File {
	private static final long serialVersionUID = 1L;
	private byte[] hash;
	private byte[] fileFormat;
	private final static int PACKETSIZE = 1024;
	private int lastPacketSize;
	private int numberOfPackets;
	private int numberOfPacketsReceived;
	private static RandomAccessFile myRandomAccessFile;
	/** This Constructor is used for creating DataMessages. It parses the original files into DataMessages of a given length.
	* @param path Path to the file you want to create 
	**/
	
	public DataFile(String path) {
		super(path);
		this.hash = this.computeHash();
		try {
			this.setFormat();
		} catch (UnsupportedEncodingException e) {
			System.err.println("No ASCII on this System ? No way");
		}
		// Having 0 packetsReceived means we initialized it with a file on the disk not fetched from the network
		numberOfPacketsReceived = 0;
		
	}
	
	/** This Constructor is used to build a file from a bunch of DataMessages.
	* @param path This sets the name of the file we want to build
	* @param firstDataBlock This is the first DataMessage that should be written to the drive. 
	**/

	public DataFile(String path, DataMessage firstDataBlock) {
		super (path + "." + new String(firstDataBlock.getFormat()));
		fileFormat = firstDataBlock.getFormat();
		numberOfPackets = (int) ((firstDataBlock.getfileSize() / PACKETSIZE) +1);
		hash = firstDataBlock.getHash();
		numberOfPacketsReceived = 1;
		
		try {
			myRandomAccessFile = new RandomAccessFile(path + "." + new String(firstDataBlock.getFormat()), "rw");
			myRandomAccessFile.seek(firstDataBlock.getContinuation() * PACKETSIZE);
			myRandomAccessFile.write(firstDataBlock.getPayload());
			myRandomAccessFile.close();

		} catch (FileNotFoundException e) {
			System.err.println("Wrong Access Path");

		} catch (IOException e) {
			System.err.println("I/O Issue (Do you have all the permissions ?)");
		}
	}
	
	/** This method returns the block of data at a given position in the file. It will always return packages of size PACKETSIZE.
	 * @param offset The offset defines what block exactly should be returned.
	 * @return The requested block of data.
	**/

	public byte[] getBlock(int offset) throws IOException, NoSuchAlgorithmException {
		byte[] data = null;
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(this));
		stream.read(data, offset, PACKETSIZE);
		return data;
	}
	
	/** This is a getter method for the boolean variable that defines the completion of a file writing.
	 * @return This variable defines whether or not a received file is complete.
	**/

	public boolean isComplete() {

		if (Arrays.equals(this.computeHash(), this.hash)) {
			return true;
		}
		return false;
	}
	
	/** This method allow to get a hash of a complete DataFile. No hash of individual packages is computed. So if the hash computed originally and then on the receiver's side don't match, the whole file needs to be resent.
	 * @return byte array of the hash
	**/

	public byte[] computeHash() {
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(this);
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find self. Weird");
			fis = null;
		}
		MessageDigest hasher;
		try {
			hasher = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("SHA-1 is not installed");
			hasher = null;
		}
		byte[] dataBytes = new byte[1024];

        int nread = 0; 
        try {
			while ((nread = fis.read(dataBytes)) != -1) {
			  hasher.update(dataBytes, 0, nread);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
        byte[] mdbytes = hasher.digest();
 
        return mdbytes;

	}

	/** Getter method for the Hash
	 * @return Computer or received hash of a file
	**/
	
	public byte[] getHash() {	
		return this.hash;
	}	
	
	/** Getter for the format of a file. If we received this file, the format is already defined in the DataMessages.
	 * If we got the file from the file system, we need to set the format first.
	 * @return byte array of the format
	**/
	
	public byte[] getFormat() {
		return fileFormat;	
	}
	
	/** Setter of the file format for file system files.
	**/
	
	public void setFormat() throws UnsupportedEncodingException {
		String fileFormatString = this.getName();
		String[] splitStrings = fileFormatString.split("\\.");
		fileFormatString = splitStrings[(splitStrings.length)-1];
		fileFormatString.toLowerCase();
		
		if (fileFormatString.length() < 4){
			for (int i = fileFormatString.length() ; i < 4; i++){
				fileFormatString = fileFormatString + " ";
			}
		}
		
		if (fileFormatString.length()>4){
			for (int i = fileFormatString.length() ; i > 4; i--){
				fileFormatString = fileFormatString.substring(0, (fileFormatString.length()-2));
			}
		}
		
		fileFormat = fileFormatString.getBytes("ASCII");
	}

	/** Method allowing to write packages to the file.
	 * @return byte array of the format
	**/
	
	public void writePacket(DataMessage block) {
		
		if (!Arrays.equals(hash, block.getHash()))
			throw new IllegalArgumentException("Wrong fichier.");

		if (!isValideSize(block))
			throw new IllegalArgumentException("Bad size.");
		
		lastPacketSize = block.getfileSize();
		try {
			myRandomAccessFile = new RandomAccessFile(this, "rw");
			myRandomAccessFile.seek(block.getContinuation() * PACKETSIZE);
			myRandomAccessFile.write(block.getPayload());
			numberOfPacketsReceived++;
			myRandomAccessFile.close();
		} catch (FileNotFoundException e) {
			System.err.println("The DataFile should exist.");
		}
		catch (IOException e) {
			System.err.println("Seems like we don't have the required permissions");
		}
	}
	
	public int numberOfPacketsReceive() {
		return numberOfPacketsReceived;
	}
	
	
	public void writeToOutputStream(String planeID, DataOutputStream out) {
		try {
			myRandomAccessFile = new RandomAccessFile(this.getAbsolutePath(), "r");
			long fileSize = this.length();
			int continuation = -1;
			byte[] payload = new byte[PACKETSIZE];

			if (this.length() > PACKETSIZE) {
				while (continuation < fileSize / PACKETSIZE) {
					myRandomAccessFile.read(payload);
					continuation++;
				 new DataMessage(planeID.getBytes("ASCII"),continuation, 0, 0,hash, fileFormat,
							payload.length, payload).write(out);

				}
			}
			
			payload = new byte[(int) (fileSize % (PACKETSIZE))];
			myRandomAccessFile.read(payload);
			new DataMessage(planeID.getBytes("ASCII"), continuation, 0, 0, hash, fileFormat,
					payload.length, payload).write(out);

			myRandomAccessFile.close();
		} catch (FileNotFoundException e) {
			System.err.println("The DataFile should exist.");
		}
		catch (IOException e) {
			System.err.println("Seems like we don't have the required permissions");
		}
	}
	
	@Override
	public String getName(){
		if (numberOfPacketsReceived == 0){
			return super.getName();
		}
		else {
			String fileFormatString = new String (fileFormat);
			fileFormatString = fileFormatString.replaceAll("\\s","");
			return (super.getName() + "." +fileFormatString);
		}
	}
	
	
	private boolean isValideSize(DataMessage dataMessage) {
		if (dataMessage.getPayload().length > DataMessage.MAX_PACKET_SIZE)
			return false;
		if (dataMessage.getPayload().length == DataMessage.MAX_PACKET_SIZE
				&& dataMessage.getContinuation() * DataMessage.MAX_PACKET_SIZE > dataMessage.getfileSize())
			return false;
		if (dataMessage.getPayload().length < DataMessage.MAX_PACKET_SIZE
				&& ((dataMessage.getContinuation()) * DataMessage.MAX_PACKET_SIZE + dataMessage
						.getPayload().length) != dataMessage.getfileSize())
			return false;

		return true;

	}

	
}