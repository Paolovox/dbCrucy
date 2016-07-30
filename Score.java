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

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

public class Score 
{
	static private Score instance;
	private static MongoClient mongoClient;
	private MongoCollection<Document> users;
	public static MongoDatabase md;
	
	private final String SERVER = "localhost";
	private final int PORT = 27017;

	
	private void newDb()
	{
		mongoClient = new MongoClient(SERVER, PORT);
		MongoDatabase db = mongoClient.getDatabase("score");
		users = db.getCollection("users");
	}

//    public static MongoDatabase getDB() {
//        if(mongoClient == null)
//            throw new IllegalStateException("Client not initialized!");
//
//        if(md == null) {
//            md = mongoClient.getDatabase("my_db");
//        }
//        return md;
//    }
	
    public void visualizzeDb()
    {		
    	for (Document cur : users.find())
    		System.out.println(cur.toJson());
    }
    
    public boolean findUser(String idUser) { return users.count(new BasicDBObject().append("_id", idUser)) != 0; }
    
	public boolean remove(String idUser) 
	{
		if (!findUser(idUser))	return false;
		DeleteResult deleteResult = users.deleteOne(new BasicDBObject().append("_id", idUser));
		return deleteResult.getDeletedCount() != 0;
	}
	
	public void toJson()
	{
		Document user = users.find().first();
		System.out.println(user.toJson());
	}
	
	static public Score getInstance()
	{
		if (instance == null) instance = new Score();
		return instance;
	}
	
	private Score() { newDb(); }
	
	public long getNumberOfUsers() { return users.count(); }
	
	public int getScore(String idUser)
	{
		if(!findUser(idUser)) return 0;
		String score = users.find(new BasicDBObject().append("_id", idUser)).projection(fields(include("score"), excludeId())).first().toString();
		return Integer.parseInt(score.substring(16, score.indexOf("}}")));
	}
	
	public void setScore(String idUser, int score)
	{
		if (findUser(idUser))
			remove(idUser);
		Document user = new Document()
				.append("_id", idUser)
				.append("score", score);
		users.insertOne(user);

	}
}
