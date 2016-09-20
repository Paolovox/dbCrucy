
import org.json.*;



public class JsonExample {

	public static void main(String[] args) throws JSONException {
		
	
		
		String s = "{\"Altezza\":10,\"Data\":\"Thu Aug 25 10:53:11 CEST 2016\""
				+ ",\"Boolean\":true,\"Dominio\":\"Informatica\",\"Larghezza\":23}";
		
		
		JSONObject jo = new JSONObject(s);
		
		System.out.println("Altezza = "+jo.getInt("Altezza")+"\n"
						+ "Data = "+jo.getString("Data")+"\n"
						+ "Boolean = "+jo.getBoolean("Boolean")+"\n"
						+ "Dominio = "+jo.getString("Dominio")+"\n"
						+ "Larghezza ="+jo.getInt("Larghezza")
							);
		
	}
	
	
}
