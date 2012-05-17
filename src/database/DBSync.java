package database;

import generals.XYPosition;

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

	DBCollection positionsCollection;
	DBCollection logCollection ;	
	DBObject[] newPosition ;
	DBObject[] oldPosition ;
	XYPosition[] cachedPosition;
	
	@Override
	public void run() {
		
		//Initialize Database, clearing old records for the positions and logs
		
		Mongo mongoDB;
		try {
			
			mongoDB = new Mongo( "itp.fredericjacobs.com" , 27017 );
			mongoDB.dropDatabase("towerDB");
			
			DB db = mongoDB.getDB( "towerDB" );
			
			db.authenticate("fred", "fj326400".toCharArray());
			
			positionsCollection = db.getCollection("positions");
			logCollection = db.getCollection("logs");			
			newPosition = new BasicDBObject [100];
			oldPosition = new BasicDBObject[100];
			cachedPosition = new XYPosition[100];
			
			while (true){
		
				updatePositions();
				// Updating Positions every second
				Thread.sleep(1000);
				
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
		} catch (MongoException e) {
			System.out.println("MongoDB bug");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void updateLogs() {
		// TODO Auto-generated method stub
		
	}

	private void updatePositions() {
		
		for (int i=0; i< (Tower.journal.positions.size()); i++){
			oldPosition[i] = new BasicDBObject().append("planeid", Tower.journal.positions.get(i).getPlaneID()) ;
			newPosition[i] = new BasicDBObject().append("planeid", Tower.journal.positions.get(i).getPlaneID()).append("positionX", Tower.journal.positions.get(i).getPosition().getPosx()).append("positionY", Tower.journal.positions.get(i).getPosition().getPosy());
				
				if (positionsCollection.count(new BasicDBObject().append("planeid", Tower.journal.positions.get(i).getPlaneID()))<1){	
					positionsCollection.insert(newPosition[i]);
					cachedPosition[i] = new XYPosition();
					cachedPosition[i].setPosx(Tower.journal.positions.get(i).getPosition().getPosx());
					cachedPosition[i].setPosy(Tower.journal.positions.get(i).getPosition().getPosy());
				}
			
				else{
					if ((cachedPosition[i].getPosx() != Tower.journal.positions.get(i).getPosition().getPosx())  && (cachedPosition[i].getPosy() != Tower.journal.positions.get(i).getPosition().getPosy())){
						positionsCollection.update(oldPosition[i], newPosition[i]);
						cachedPosition[i].setPosx(Tower.journal.positions.get(i).getPosition().getPosx());
						cachedPosition[i].setPosy(Tower.journal.positions.get(i).getPosition().getPosy());
						System.out.println("Updated"+ " Tower X Pos : "+  Tower.journal.positions.get(i).getPosition().getPosx() + cachedPosition[i].getPosx() + Tower.journal.positions.get(i).getPosition().getPosy() + cachedPosition[i].getPosy());
					}
				}
			
			}
		
	}

}
