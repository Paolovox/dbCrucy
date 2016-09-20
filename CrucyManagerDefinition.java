import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class CrucyManagerDefinition {


	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Document> domini;	//a domain collection
	
	
	public CrucyManagerDefinition(){
		
		this.client = new MongoClient();
		this.db = client.getDatabase("parole&definizioni");
	}
	
	
	/**
	 * 
	 * @param domain
	 * @return una mappa con chiavi le parole e valore oggetti di tipo CrucyDef
	 * che rappresentano una definizione avente una difficoltà e indice
	 */
	public Map<String, List<CrucyDef>> getWords(String domain){
		Map<String, List<CrucyDef>> m = new HashMap<>();
		
		if( domain != null &&  domains().contains(domain)){
			for(Document d : db.getCollection(domain).find()){
				ArrayList<Long> ids = new ArrayList<>();
				
				List<Document> dd = (List<Document>) d.get("diff");
		
				List<CrucyDef> cd = new ArrayList<>();
				for(Document doc : dd){
					String[] ss = doc.toJson().replaceAll("[\"\\$numberLong\"{} ]", "").split("::");
					
					CrucyDef cdf = new CrucyDef(Integer.parseInt(ss[0]), Long.parseLong(ss[1]),Type.TXT);
					
					cd.add(cdf);
				}
				
				String[] img = d.get("img").toString().replaceAll("[a-zA-Z{}]", "").split("=");
		
				
				m.put(d.getString("word"), cd);
			}
		}	
		return m;
	}
	
	
	/**
	 * 
	 * @return una lista di tutti i domini presenti nel db "parole&definizioni"
	 */
	public List<String> domains(){
		List<String> domains = new ArrayList<>();
		for(String s : db.listCollectionNames()){
			domains.add(s);
		}
		
		return domains;
	}
	
	
	//Test
	public static void main(String[] args) {
		
		CrucyManagerDefinition ma = new CrucyManagerDefinition();
		
		//domini
		System.out.println(ma.domains());
		//parole di un dominio
		System.out.println(ma.getWords("Meteorology"));
		
	}
}
