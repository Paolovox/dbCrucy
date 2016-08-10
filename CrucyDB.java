import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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


public class CrucyDB {
	
	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Document> crucy;
	
	
	
	public CrucyDB()
	{
		client = new MongoClient(); //default "localhost" "27017"
		db = client.getDatabase("Crucy");
		crucy = db.getCollection("cruciverba");
		
	
	}
	
	/**
	 * Inserisce un Cruciverba con in suoi attributi nel db
	 * 
	 * @param dom - Dominio
	 * @param diff - Difficoltà
	 * @param cru - Cruciverba
	 */
	public void insert(String dom, double diff, String cru)
	{
		Document doc = new Document()
				.append("dominio", dom)
				.append("difficoltà", diff)	//attributi 
				.append("cruciverba", cru);
		
		crucy.insertOne(doc);
	}
	
	/**
	 * Cerca un Cruciverba nel db in base al dominio 
	 * e la difficoltà. La lista ids fa si che venga prelevato un 
	 * cruciverba nuovo con un id diverso da quelli nella lista.
	 * @param dom
	 * @param diff
	 * @param ids: lista degli ObjectId dei crucy già utilizzati
	 * @return la stringa del Cruciverba o null se non è presente
	 */
	public String find(String dom, double diff, List<ObjectId> ids)
	{
		
		Bson filter = new Document("dominio",dom).append("difficoltà", diff)
				.append("_id", new Document("$nin",ids ));
		Bson project = new Document("cruciverba",1).append("_id", 1);
		
		String r =  crucy.find(filter).projection(project).first().toJson();
		
		return r;
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
		for(Document d : crucy.find())
			System.out.println(d.toJson());	
	}
	
	
	//TEST: insert - visualize - find
	public static void main(String[] args) {
		CrucyDB c = new CrucyDB(); // istanzia un client per il db dei cruciverba
		c.insert("Informatica", 0.5, "***/n***/n***");
		c.visualizeDB();
		List<ObjectId> id = new ArrayList<>(Arrays.asList(new ObjectId("57a25754427a2522a02ebeed")));
		System.out.println(c.find("Informatica", 0.5, id));
		c.closeDB();
	}
}
