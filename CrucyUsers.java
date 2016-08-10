import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

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
	 * @param idUser
	 * @return true se l'utente esiste, false altrimenti
	 */
	public boolean findUser(String idUser) 
	{
		return users.count(new BasicDBObject().append("_id", idUser)) != 0; 
	}
	
	
	/**
	 * 
	 * @param idUser
	 * @return true se l'utente viene eliminato correttamente, false altrimenti
	 */
	public boolean remove(String idUser) 
	{
		if (!findUser(idUser))	return false;
		DeleteResult deleteResult = users.deleteOne(new BasicDBObject().append("_id", idUser));
		return deleteResult.getDeletedCount() != 0;
	}

	/**
	 * 
	 * @return il numero di utenti
	 */
	public long getNumberOfUsers() { return users.count(); }
	
	
	/**
	 * Assegna un punteggio ad un utente
	 * @param idUser
	 * @param score
	 * 
	 */
	public void setScore(String idUser, int score)
	{
		if (findUser(idUser))
			remove(idUser);
		Document user = new Document()
				.append("_id", idUser)
				.append("score", score);
		users.insertOne(user);
	}
	
	
	/**
	 * 
	 * @param idUser
	 * @return ritorna il punteggio di un utente, -1 in caso di errore
	 */
	public int getScore(String idUser)
	{
		if(!findUser(idUser)) return -1;
		String score = users.find(new BasicDBObject().append("_id", idUser)).projection(fields(include("score"), excludeId())).first().toString();
		return Integer.parseInt(score.substring(16, score.indexOf("}}")));
	}
	
	/**
	 *  Stampa a video il contenuto del db
	 */
	public void visualizeDB()
	{
		for(Document d : users.find())
			System.out.println(d.toJson());	
	}
	
	
	/**
	 * chiude la connessione al db del client
	 */
	public void closeDB(){
		client.close();
}
	
}
