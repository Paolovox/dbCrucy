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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;


public class MongoExample {
	
	public static void main(String[] args) {
		
	CrucyUser user = new CrucyUser();
	user.drop();
	CrucyDB crucy = new CrucyDB();
	crucy.drop();
	
	user.registerUser("paolo", "pablitos23");
	user.registerUser("anna", "240567");
	user.registerUser("paolo", "23031992");
	user.registerUser("giovanni", "ciao");
	user.registerUser("nicola", "ciao");
	user.registerUser("ziPippo", "we");
	user.setScore("giovanni", 10);
	user.setScore("ziPippo", 777);
	user.setScore("nicola", 666);
	
	System.out.println("Find="+user.findUser("paolo"));
	System.out.println("Login="+user.login("paolo", "pablitos23"));
	user.remove("paolo");
	System.out.println("Login="+user.login("paolo", "pablitos23"));
	
	user.setScore("anna", 50);
	user.setScore("paolo", 666);
	
	System.out.println(user.getScore("anna"));
	
	System.out.println("=== CLASSIFICA ===");
	System.out.println(user.getRanking(4));
	
	System.out.println("Ultimo="+user.getUltiPosition());
	
	
	System.out.println("=== DB UTENTI ===");
	System.out.println("Count="+user.getNumberOfUsers());
	user.visualizeDB();
	
	System.out.println("=== DB CRUCY ===");
	crucy.visualizeDB();
		
		
		
	}
	
	
	
	
}
