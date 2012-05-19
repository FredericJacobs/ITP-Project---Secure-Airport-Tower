package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import messaging.Plane;
import messaging.Visitor;

public class DataMessage extends Message implements VisitorMessage{
	public static final int MAX_PACKET_SIZE = 1024; 
	byte[] hash; // 20 octets (20 bytes) 
	int continuation;
	byte[] format; // 4 octets (4 bytes) 
	int fileSize;
	byte[] payload;
	
	public DataMessage (byte[] planeID, int continuation, int posx,
			int posy, byte [] hash, byte[] format, int fileSize, byte[] payload) {
		super (planeID, payload.length, 4, posx, posy, MessageType.DATA);
		this.continuation = continuation;
		this.hash = hash ;
		this.format = format ; 
		this.fileSize = fileSize ; 
		this.payload = payload; 
	}
	public byte[]  getPayload() {
		return this.payload;
	}
	
	public int getfileSize(){
		return fileSize;
	}
	
	public byte [] getFormat (){
		return format;
	}
	
	public byte[] getHash (){
		return hash;
	}
	
	public int getContinuation (){
		return continuation;
	}	
	public void write(DataOutputStream out) throws IOException{
		super.write(out);
		out.write(hash);
		out.writeInt(continuation);
		out.write(format);
		out.writeInt(fileSize);
		out.write(payload);
	}
	@Override
	public int accept(Visitor visitor,Plane plane,DataOutputStream outData){
		 return visitor.visit(plane,this,outData);						
	}
}
