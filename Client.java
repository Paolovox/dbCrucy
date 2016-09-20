import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import it.uniroma1.lcl.cruciverba.Cruciverba;

public class Client {
	
	private boolean sendGet(int altezza, int larghezza) throws Exception{
		
		String urlParameters = "?altezza="+altezza+"&larghezza="+larghezza;
		String url = "http://93.88.106.32/Crucy/"+urlParameters;
		
		URL obj = new URL(url);	
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		
		boolean status =  responseCode == HttpURLConnection.HTTP_OK;
		
		//deserializzazione, ricevo dal server il flusso di byte contenente l'oggetto Cruciverba già istanziato
		ObjectInputStream oo = new ObjectInputStream(con.getInputStream());
		Cruciverba ob = (Cruciverba) oo.readObject(); 
		
		System.out.println(ob);
		ob.salva("MioCrucy.doc"); 
		
		return status;
	
	}
	
	public static void main(String[] args) throws Exception {
		

		if(args.length<2){
			System.out.println("Usage: <height> && <weight>!!!");
			return;
		} 
		
		int a1 = Integer.parseInt(args[0]);
		int a2 = Integer.parseInt(args[1]);
		
		if(a1 < 2 || a1 > 5 || a2 < 2 || a2 > 5){
			System.out.println("Per il momento solo crucy tra 2x2 e 5x5 a schema libero :( !!!");
			return;
		}
	
		
		
		Client c = new Client();
		c.sendGet( a1,a2);
		
	}
	
}
