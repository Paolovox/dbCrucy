
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CrucyDefinitionReader {

	boolean DEBUG = false;
	
	private Scanner scanner;
	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Document> domini;	//a domain collection
	private MongoCollection<Document> definizioni; //collection id:def
	private static long id;	
	private PrintWriter fw;
	
	
	/**
	 * La classe CrucyReader legge il file delle parole e definizioni e 
	 * crea una collezione in MongoDB nel db "parole&definizioni"
	 * per ogni dominio. Ogni dominio contiene una lista di documenti con attributi: 
	 * parola-difficoltà-img.
	 *  
	 * Es:
	 *================================ DOMINIO1 =====================================
	 *| parola |----------| difficoltà --------|------ |img                        |
	 *|- word  |          |- lv dif1 -> id def |       |- empty or lv dif -> id URL|
	 *|________|          |- lv dif2 -> id def2|       |___________________________|
	 *                    |- ....______________|
	 * ==============================================================================
	 * 
	 *                    
	 * Contemporaneamente indicizza le definizioni (multimediali e non) in una 
	 * collezione "definizioni" del db "parole&definizioni"
	 * =================
	 * ID -> DEFINIZIONE
	 * =================
	 * 
	 *
	 * 
	 * @param path del file delle parole e definizioni
	 * @throws IOException 
	 */
	public CrucyDefinitionReader(String path) throws IOException{
		
		this.scanner = new Scanner(new File(path));
		this.client = new MongoClient();
		this.db = client.getDatabase("parole&definizioni");
		
		if(DEBUG) {
			db.drop();
			this.fw  = new PrintWriter("debug.txt","UTF-8");
			
		}
		
		this.definizioni = db.getCollection("definizioni");
		
		Document doc = new Document("id_def",1).append("unique", true);
		definizioni.createIndex(doc);
		
	}
	
	/**
	 * Legge parole e definizioni dal file, e crea tutte le collezioni
	 * sopra descritte nel db "parole&definizioni"
	 * @throws IOException
	 */
	public void read() throws IOException{
		int i=0;
		while(scanner.hasNext() &&  i<30){
		i++;
			String line = scanner.nextLine();
			String[] token = line.split("\\t");
			
			if(token.length != 7) continue;
			
			String word = token[1];
			String domain = token[5];
			double diff = Double.parseDouble(token[3]);
			String def = token[2];
			boolean type = token[6].equals("true") ? true : false;
			
			if(DEBUG){
			System.out.println("Parola="+word+""
					+ " Dominio="+domain+""
					+ " Diff="+diff+""
					+ " "+(type==true?"URL="+def:"Def="+def) );
			}
			
			insertIntoDomain(word,domain,diff,def,type);		
		}
		
		
		
		if(DEBUG){
			System.out.println(" === DEFINIZIONI === \n");
			System.out.println(printCollection("definizioni"));
			System.out.println("===========================");
			
			fw.write(" === DEFINIZIONI === \n");
			fw.write(printCollection("definizioni"));
			fw.write("===========================\n\n\n\n\n\n\n");
			
			for(String d : db.listCollectionNames()){
				if(!d.equals("definizioni")){
					System.out.println(" === "+d+" ===");
					System.out.println(printCollection(d));
					System.out.println("======================");
					
					fw.write(" === "+d+" ===\n\n\n");
					fw.write(printCollection(d));
					fw.write("===========================\n\n\n");
				}
			}
		}	
		
		System.out.println("OK: indexing has been successful");
	}
	
	/**
	 * Crea collezione dominio se non esiste altrimenti usa quella già esistente.
	 * Inserisce nella collezione le parole con gli indici delle definizioni associate alle difficoltà,
	 * oppure l'immagine.
	 * Man mano che preleva le definizioni, le indicizza anche.
	 * 
	 * @param word
	 * @param domain
	 * @param diff
	 * @param def
	 * @param type definition. true->img, false->text
	 */
	private void insertIntoDomain(String word, String domain, double diff, String def, boolean type){
		
		domini = db.getCollection(domain);
		Document indx = new Document("word",1);
		domini.createIndex(indx);
		
		
		String img = type==true? def : null;
		
		if(existWord(word, domain)){
			if(!type){
				domini.updateOne(new Document("word",word), 
						new Document("$push", new Document("diff" ,
								new Document(convertDiff(diff), insertDef(def)))));
			}
			else{
				domini.updateOne(new Document("word",word), 
						new Document("$set", new Document( "img" ,
								new Document(convertDiff(diff), insertDef(img)))));
			}	
		}
		else{
			if(!type){
				List<Document> docs = new ArrayList<Document>();
				docs.add(new Document( convertDiff(diff), insertDef(def)));
			
				Document doc = new Document("word",word)
						.append("diff", docs)
						.append("img", null);
			
				domini.insertOne(doc);
			}
			else{
				Document doc = new Document("word",word)
						.append("diff", new ArrayList<Document>())
						.append("img", new Document(convertDiff(diff), insertDef(img)));
			
				domini.insertOne(doc);
			}
		}
	}
	
	/**
	 * Indicizza le definizioni della collection "definizioni"
	 * 
	 * @param def
	 * @return
	 */
	private long insertDef(String def){
		Document doc = new Document("id_def",id)
				.append("def", def);
		
		definizioni.insertOne(doc);
		return id++;
	}
	
	
	/**
	 * 
	 * @param name
	 * @return la stringa della collezione di nome "name"
	 */
	private String printCollection(String name){
		String c="";
		Document proj = new Document("_id", 0);
		
		for(Document d : db.getCollection(name).find().projection(proj)){
			c+=d.toJson()+"\n";
		}
		return c;
	}
	
	/**
	 * MongoDB non accetta come valore di chiave ne float ne double.
	 * Questo metodo converte l'intervallo di difficoltà da
	 * [0.0 - 1.0] -> [0 - 10]
	 * @param diff
	 * @return la difficoltà
	 */
	private String convertDiff(double diff){
		String s = "";
		String d = diff+"";
		
		if(diff == 1.0) s+=10;
		else s+= d.substring(2);
		
		return s;
	}
	
	
	/**
	 * 
	 * @param w
	 * @param domain
	 * @return true se la parola già esiste in un determinato dominio
	 */
	private boolean existWord(String w, String domain){
		return db.getCollection(domain).count(new Document("word",w)) > 0;
	}
	
	
	/**
	 * Impostare la modalità debug
	 * @param debug
	 */
	public void setDebug(boolean debug){
		this.DEBUG = debug;
	}
	
	
	/**
	 * 
	 * @return la stringa delle definizioni indicizzate con i rispettivi indici
	 */
	public String toStringDef(){
		String s="";
		for(Document d : db.getCollection("definizioni").find()){
			s+=d.toJson();
		}
		return s;
	}
	
	
	//Test
	public static void main(String[] args) throws IOException {
		
		long time1 = System.currentTimeMillis();
		CrucyDefinitionReader cr = new CrucyDefinitionReader("definizioni_ita.txt");
		cr.read();
		long time2 = System.currentTimeMillis();
		
		System.out.println((time2-time1) /1000 +"s");
	}
	
	
}
