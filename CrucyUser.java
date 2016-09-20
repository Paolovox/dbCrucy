import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/*  This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

public class CrucyUser {

	
	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Document> users;
	
	
	
	public CrucyUser()
	{
		client = new MongoClient(); //default "localhost" "27017"
		db = client.getDatabase("Crucy");
		users = db.getCollection("users");
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return true se l'utente si è registrato correttamente, altrimenti
	 * 		   false se l'username dell'utente già è esistente.
	 */
	public boolean registerUser(String username, String password)
	{
		if (findUser(username) ) return false;
		
		Document user = new Document()
				.append("username", username)
				.append("password", password);
			
		users.insertOne(user);
		return true;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return true se l'utente esiste nel db, false altrimenti
	 */
	public boolean login(String username, String password)
	{
		if(findUser(username))
		{
			Bson filter = new Document("username", username)
						.append("password", password);
			
			return users.find(filter).first() != null;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param username
	 * @return true se l'utente esiste, false altrimenti
	 */
	public boolean findUser(String username) 
	{
		return users.count(new Document().append("username", username)) != 0; 
	}
	
	
	/**	 * 
	 * @param username
	 * @return true se l'utente viene eliminato correttamente, false altrimenti
	 */
	public boolean remove(String username) 
	{
		if (!findUser(username))	return false;
		DeleteResult deleteResult = users.deleteOne(new BasicDBObject().append("username", username));
		return deleteResult.getDeletedCount() != 0;
	}

	/**
	 * 
	 * @return il numero di utenti
	 */
	public long getNumberOfUsers() { return users.count(); }
	
	
	/**
	 * Assegna un punteggio ad un utente se esiste
	 * @param username
	 * @param score
	 * 
	 */
	public boolean setScore(String username, long score)
	{	
		boolean f=false;
		if (findUser(username)){
		
			users.updateOne(new Document("username", username), 
							new Document("$set", new Document("score", score)));
			
			f=!f;
		}
		return f;
	}
	
	
	/**
	 * 
	 * @param username
	 * @return ritorna il punteggio di un utente, -1 in caso di errore
	 */
	public long getScore(String username)
	{
		if(!findUser(username)) return -1;
		String score = users.find(new BasicDBObject().append("username", username)).projection(fields(include("score"), excludeId())).first().toString();
		return Long.parseLong(score.substring(16, score.indexOf("}}")));
	}
	
	
	/**
	 * 
	 * @param limit: indica quante posizioni si vogliono prelevare a partire dalla prima posizione
	 * @return una mappa Score - User
	 */
	public Map<Long, String> getRanking(int limit){
		
		Map<Long, String> m = new TreeMap<Long, String>(Collections.reverseOrder());
		
		Iterator<Document> cursor = users.find().sort(new Document("score",-1)).limit(limit).iterator();
		
		while(cursor.hasNext()){
			Document d = cursor.next();
			m.put(d.getLong("score"),d.getString("username"));
		}
		
		return m;
	}
	
	/**
	 * 
	 * @return Score - User dell'ultima posizione
	 */
	public Map<Long, String> getUltiPosition()
	{
		Map<Long, String> m = new TreeMap<Long, String>();
		Document d = users.find().sort(new Document("score",1)).first();
		m.put(d.getLong("score"),d.getString("username"));
		return m;
	}
	
	/**
	 * Elimina tutti i document dal db
	 */
	public void drop(){
		db.drop();
	}
	
	/**
	 * chiude la connessione al db del client
	 */
	public void closeDB(){
		client.close();
	}
	
	/**
	 *  Stampa a video il contenuto del db
	 */
	public void visualizeDB()
	{
		for(Document d : users.find())
			System.out.println(d.toJson());	
	}
	
	
}
