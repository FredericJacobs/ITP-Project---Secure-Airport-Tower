package database;

import java.net.UnknownHostException;

import messaging.Tower;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;

public class DBSync implements Runnable  {

	@Override
	public void run() {
		
		//Initialize Database, clearing old records for the positions and logs
		
		Mongo mongoDB;
		try {
			
			mongoDB = new Mongo( "itp.fredericjacobs.com" , 27017 );
			mongoDB.dropDatabase("towerDB");
			
			DB db = mongoDB.getDB( "towerDB" );
			
			db.authenticate("fred", "fj326400".toCharArray());
			
			DBCollection positionsCollection = db.getCollection("positions");
			DBCollection logCollection = db.getCollection("logs");

			this.updatePositions();
			this.updateLogs();
			
			while (true){
				DBObject position = new BasicDBObject();
			
				for (int i=0; i< (Tower.planes.size()); i++){
				position.put("planeid", Tower.planes.get(i).getPlaneID());
				position.put("positionX", Tower.planes.get(i).getPosX());
				position.put("positionY", Tower.planes.get(i).getPosY());
				}
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
		} catch (MongoException e) {
			System.out.println("MongoDB bug");
		}
		
		
	}

	private void updateLogs() {
		// TODO Auto-generated method stub
		
	}

	private void updatePositions() {
		// TODO Auto-generated method stub
		
	}

}
