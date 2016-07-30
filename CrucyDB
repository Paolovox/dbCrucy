import org.bson.Document;
import org.bson.conversions.Bson;
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

	
	private static CrucyDB instance;
	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Document> crucy;
	
	
	
	private CrucyDB()
	{
		client = new MongoClient(); //default "localhost" "27017"
		db = client.getDatabase("Crucy");
		crucy = db.getCollection("cruciverba");
	}
	
	
	static public CrucyDB getInstance()
	{
		if (instance == null) instance = new CrucyDB();
		return instance;
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
	 * Consente di cercare un Cruciverba nel db in base al dominio 
	 * e la difficoltà.
	 * @param dom
	 * @param diff
	 * @return la stringa del Cruciverba o null se non è presente
	 */
	public String find(String dom, double diff)
	{
		Bson filter = new Document("dominio",dom).append("difficoltà", diff);
	//	Bson project = new Document("cruciverba",1).append("_id", 0);
		
		return crucy.find(filter).first().getString("cruciverba");
		
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
		CrucyDB c = CrucyDB.getInstance();
		c.insert("Informatica", 0.5, "***/n***/n***");
		c.visualizeDB();
		System.out.println(c.find("Informatica", 0.5));
	}
}
