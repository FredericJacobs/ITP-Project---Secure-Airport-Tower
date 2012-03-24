package DataFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import messaging.messages.*;


public class DataFile extends File {
	private static final long serialVersionUID = 1L;
	private byte[] hash;
	private byte[] payload;
	private byte[] fileFormat;
	private final static int PACKETSIZE = 1024;
	private int lastPacketSize;
	private int numberOfPackets;
	private String pathToFile;
	private boolean isComplete;
	private FileOutputStream writingOutputStream ;
	private byte[] payLoads[];
	private int numberOfPacketsReceived;
	
	public DataFile(String path) throws NoSuchAlgorithmException, IOException {
		super("bin"+File.separator +"tests" + File.separator + path);
		this.pathToFile = "bin"+File.separator+"tests" + File.separator + path;
		numberOfPackets = (int) ((this.length() / PACKETSIZE)+1);
		hash = computeHash();
		
		isComplete = true;
		
	}
	

	public DataFile(String path, DataMessage firstDataBlock) throws NoSuchAlgorithmException, IOException {
		super ("bin"+File.separator +"tests" + File.separator + path);
		this.pathToFile = "bin"+File.separator+"tests" + File.separator + path;
		fileFormat = firstDataBlock.getFormat();
		numberOfPackets = (int) ((firstDataBlock.getfileSize() / PACKETSIZE) +1);
		hash = firstDataBlock.getHash();
		if (firstDataBlock.getContinuation() == -1){
			isComplete = true ;
		}
		else {isComplete = false ;}
		
		payLoads = new byte[DataMessage.MAX_PACKET_SIZE] [numberOfPackets];
		numberOfPacketsReceived = 0;
		writingOutputStream = new FileOutputStream (pathToFile);
		
	}


	public byte[] getBlock(int offset) throws IOException, NoSuchAlgorithmException {
		byte[] data = null;
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(this));
		stream.read(data, offset, PACKETSIZE);
		return data;
	}
	
	public void save(DataMessage msgFile) throws IOException {
		RandomAccessFile file = new RandomAccessFile(this.getCanonicalFile(), "rw");
		file.writeBytes(msgFile.getPayload().toString());
	}
	
	public boolean isComplete() {
		return isComplete;
	}

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

	public byte[] getHash() {	
		return this.hash;
	}	
	
	public byte[] getFormat() throws UnsupportedEncodingException {
		setFormat ();
		return fileFormat;
		
	}
	
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

	public void writePacket(DataMessage block) throws IOException {
		numberOfPacketsReceived ++;
		System.out.println(numberOfPackets);
		System.out.println(numberOfPacketsReceived);
		if (numberOfPacketsReceived < numberOfPackets){
			payLoads[block.getContinuation()] = block.getPayload() ;
		}
		
		if (numberOfPacketsReceived == (numberOfPackets-1)){
			payLoads[block.getContinuation()] = block.getPayload() ;
			for (int i = 0; i < payLoads.length; i++){
				writingOutputStream.write(payLoads[i]);
			}
			isComplete = true;
		}
	   
	}


	public String getPathToFile() {
		return pathToFile;
	}

}
