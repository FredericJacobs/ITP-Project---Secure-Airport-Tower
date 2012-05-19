package database;

import generals.XYPosition;

import java.net.UnknownHostException;

import messaging.Tower;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/** Description of DBSync 
 * DB Sync is a separate thread that is taking care for updating the MongoDB database.
 * @author Frederic Jacobs
 * @author Hantao Zhao 
 * @version 1.0
 */

public class DBSync implements Runnable  {

	DBCollection positionsCollection;
	DBCollection logCollection ;	
	DBObject[] newPosition ;
	DBObject[] oldPosition ;
	XYPosition[] cachedPosition;

	@Override
	public void run() {

		/** MongoDB is a No-SQL document-oriented database.
		 * We start by connecting to it directly (potentially unsafe). And drop all previous information from older executions we don't need.
		 * The goal here is to store a single execution of the tower. 
		 */
		Mongo mongoDB;
		try {

			mongoDB = new Mongo( "itp.fredericjacobs.com" , 27017 );
			DB db = mongoDB.getDB( "towerDB" );
			db.authenticate("fred", "fj326400".toCharArray());
			mongoDB.dropDatabase("towerDB");

			//Collections are the equivalent of what tables are in relational databases.
			
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
			System.out.println("Probably issues connecting. You may have connection issue or the server may be down");
		} catch (MongoException e) {
			System.out.println("MongoDB bug");
		} catch (InterruptedException e) {
			System.exit(0);
		}


	}

	private void updateLogs() {
		// TODO Auto-generated method stub

	}

	private void updatePositions() {

		for (int i=0; i< (Tower.journal.positions.size()); i++){
			//oldPosition is used to query the DB with the planeid to search for current records
			oldPosition[i] = new BasicDBObject().append("planeid", Tower.journal.positions.get(i).getPlaneID()) ;
			newPosition[i] = new BasicDBObject().append("planeid", Tower.journal.positions.get(i).getPlaneID()).append("positionX", Tower.journal.positions.get(i).getPosition().getPosx()).append("positionY", Tower.journal.positions.get(i).getPosition().getPosy());

			//If this planeid is not yet in the database, we add it
			if (positionsCollection.count(new BasicDBObject().append("planeid", Tower.journal.positions.get(i).getPlaneID()))<1){	
				positionsCollection.insert(newPosition[i]);
				cachedPosition[i] = new XYPosition();
				cachedPosition[i].setPosx(Tower.journal.positions.get(i).getPosition().getPosx());
				cachedPosition[i].setPosy(Tower.journal.positions.get(i).getPosition().getPosy());
			}

			else{
				//Let's only update the position if it is truly required.
				if ((cachedPosition[i].getPosx() != Tower.journal.positions.get(i).getPosition().getPosx())  && (cachedPosition[i].getPosy() != Tower.journal.positions.get(i).getPosition().getPosy())){
					positionsCollection.update(oldPosition[i], newPosition[i]);
					cachedPosition[i].setPosx(Tower.journal.positions.get(i).getPosition().getPosx());
					cachedPosition[i].setPosy(Tower.journal.positions.get(i).getPosition().getPosy());
				}
			}

		}

	}

}
