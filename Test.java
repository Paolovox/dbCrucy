import java.util.ArrayList;


public class Test {
	
	 Runnable r = ()->{	
		 
		CrucyDB c = new CrucyDB();
		c.insert("Elettronica", 0.6, "Arduino");
		c.insert("Informatica", 0.6, "Java");
		c.insert("Algoritmi", 1.0, "Dijkstra");
		System.out.println(c.find("Informatica", 0.6, new ArrayList<>()));
		System.out.println(Thread.currentThread().getName());
		c.closeDB();		
	};
	
	
	public void genera(){
		for(int i=0; i<20; i++){ new Thread(r,""+i).start(); }
	}
	
	
	public static void main(String[] args) {
		Test t = new Test();
		t.genera();
	}

}
