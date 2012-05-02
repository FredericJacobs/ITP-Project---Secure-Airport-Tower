package DataFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;

import encryption.KeyPair;

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
	private String pathToFile;
	private boolean isComplete;
	private FileOutputStream writingOutputStream ;
	private byte[] payLoads[];
	private int numberOfPacketsReceived;
	private boolean owner;
	
	/** This Constructor is used for creating DataMessages. It parses the original files into DataMessages of a given length.
	* @param path Path to the file you want to create 
	**/
	
	public DataFile(String path) throws NoSuchAlgorithmException, IOException {
		super("bin"+File.separator +"LocalStorage" + File.separator + path);
		owner = true;
		this.pathToFile = "bin"+File.separator+"tests" + File.separator + path;
		if ((this.length() % PACKETSIZE) != 0){
			numberOfPackets = (int) ((this.length() / PACKETSIZE)+1);
			lastPacketSize = (int) (this.length() % PACKETSIZE); 	
		}
		else{
			numberOfPackets = (int) (this.length() / PACKETSIZE);
			lastPacketSize = 0;
		}
		hash = computeHash();
		isComplete = true;
		
	}
	
	/** This Constructor is used to build a file from a bunch of DataMessages.
	* @param path This sets the name of the file we want to build
	* @param firstDataBlock This is the first DataMessage that should be written to the drive. 
	**/

	public DataFile(String path, DataMessage firstDataBlock) throws NoSuchAlgorithmException, IOException {
		super ("bin"+File.separator +"IncomingFiles" + File.separator + path);
		String formatOfTheFile = new String (firstDataBlock.getFormat());
		owner = false;
		this.pathToFile = "bin"+File.separator +"tests" + File.separator + path + formatOfTheFile;
		fileFormat = firstDataBlock.getFormat();
		numberOfPackets = (int) ((firstDataBlock.getfileSize() / PACKETSIZE) +1);
		hash = firstDataBlock.getHash();
		if (firstDataBlock.getContinuation() == -1){
			isComplete = true ;
		}
		else {isComplete = false ;}
		
		payLoads = new byte[DataMessage.MAX_PACKET_SIZE] [numberOfPackets];
		numberOfPacketsReceived = 1;
		writingOutputStream = new FileOutputStream (pathToFile);
		writingOutputStream.write(firstDataBlock.getPayload());
	}
	
	/** The getName method had to be overridden because the given test wasn't passing the extension as part of the path. Therefore we added the file extension in the name of the file to return an accurate file name.
	 * @return Name of the file and it's extension
	**/
	
	@Override
	public String getName(){
		if (owner){
			return super.getName();
		}
		else {
			String fileFormatString = new String (fileFormat);
			fileFormatString = fileFormatString.replaceAll("\\s","");
			return (super.getName() + "." +fileFormatString);
		}
	}
	
	/** This method returns the block of data at a given position in the file. It will always return packages of size PACKETSIZE.
	 * @param offset The offset defines what block exactlty should be returned.
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
		return isComplete;
	}
	
	/** This method allow to get a hash of a complete DataFile. No hash of individual packages is computed. So if the hash computed originally and then on the receiver's side don't match, the whole file needs to be resent.
	 * @return byte array of the hash
	**/

	private byte[] computeHash() throws NoSuchAlgorithmException, IOException {
		
		FileInputStream fis = new FileInputStream(pathToFile);
		MessageDigest hasher = MessageDigest.getInstance("SHA-1");
		byte[] dataBytes = new byte[1024];
		 
        int nread = 0; 
        while ((nread = fis.read(dataBytes)) != -1) {
          hasher.update(dataBytes, 0, nread);
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
	
	public byte[] getFormat() throws UnsupportedEncodingException {
		if (owner){
			setFormat ();
		}
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
	
	public void writePacket(DataMessage block) throws IOException, NoSuchAlgorithmException {
		
		numberOfPacketsReceived ++;
		if (numberOfPacketsReceived < numberOfPackets){
			if (block.getPayload().length != DataMessage.MAX_PACKET_SIZE){
				throw new IllegalArgumentException();
			}
			else{
				payLoads[block.getContinuation()] = block.getPayload() ;
			}
		}
		
		if (numberOfPacketsReceived == numberOfPackets){
				
				payLoads[block.getContinuation()] = block.getPayload();
				
				for (int i = 0; i < payLoads.length; i++){
					writingOutputStream.write(payLoads[i]);
				}
				isComplete = true;
			
			}
	}
	
	/** Getter method for the file path.
	 * @return path to the file
	**/
	
	public String getPathToFile() {
		return pathToFile;
	}

	
}