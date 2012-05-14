package database;

import java.net.UnknownHostException;

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
			
			DB db = mongoDB.getDB( "mydb" );
			
			DBCollection positionsCollection = db.getCollection("positions");
			DBCollection logCollection = db.getCollection("logs");
			
			for (int i=0; i < 100; i++) {
			    positionsCollection.insert(new BasicDBObject().append("i", i));
			}	
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
		} catch (MongoException e) {
			System.out.println("MongoDB bug");
		}
		
		
	}

}
