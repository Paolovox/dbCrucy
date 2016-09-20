import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class IndexingMongo {

	
	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Document> col;
	private static final String DEFAULT_COLLECTION_NAME="yoMongo";
	
	
	public IndexingMongo()
	{
		client = new MongoClient(); //default "localhost" "27017"
		db = client.getDatabase("Prove");
		col = db.getCollection(DEFAULT_COLLECTION_NAME);
	//	col.drop();
	
	}
	
	
	public void init() throws JSONException{
		System.out.println("Insert...\n");
		double time1 = System.currentTimeMillis();
		for(int i=1000000; i<9000000; i++){
			Document obj = new Document()
					.append("username", "username"+i)
					.append("password", "password"+i)
					.append("age", Math.random()*100);
			
			col.insertOne(obj);
		}
		double time2 = System.currentTimeMillis();
		System.out.println("Time for insert:"+(time2-time1)/1000);
	}
	
	public void searchWithoutIndex() throws JSONException{
		System.out.println("Search...\n");
		double time1 = System.currentTimeMillis();
		Bson s = new Document("username", "username8999999");
		String ss = col.find(s).first().getString("username");
		double time2 = System.currentTimeMillis();
		System.out.println("Time for search without:"+(time2-time1)/1000);

	}
	
	public void searchWithIndex() throws JSONException{
		System.out.println("Search...\n");
		double time1 = System.currentTimeMillis();
		
		Bson indx = new Document("username",1).append("unique", true);
		col.createIndex(indx);
		
		Bson s = new Document("username", "username8999999");
		String ss = col.find(s).first().getString("username");
		
		double time2 = System.currentTimeMillis();
		System.out.println("Time for search with:"+(time2-time1)/1000);

	}
	
	public void sameInsert(){
		Document obj1 = new Document("paolo", "io");
		Document obj2 = new Document("paolo", "io");
		
		
		col.insertOne(obj1);
		col.insertOne(obj2);
		
		
		String s = col.find(obj1).first().toJson();
		ObjectId oo = new ObjectId();
		col.updateOne(obj1, new Document("$set",new Document("_id", oo)));
		System.out.println("Search...\n");
		System.out.println(s);
		
	}
	
	public void print(){
		System.out.println("\nDB");
		for(Document d : col.find()){
			System.out.println(d.toJson());
		}
	}
	public static void main(String[] args) throws JSONException {
		
		IndexingMongo mo = new IndexingMongo();
		//mo.init();
		//mo.searchWithoutIndex();
		//mo.searchWithIndex();
		
		mo.sameInsert();
		mo.print();
		
		
		
		
	}
	
}
