import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrucyGen {

	private CrucyDB cdb = new CrucyDB();
	private List<CrucyThread> lct = new ArrayList<>();
	
	
	public synchronized void generaT(long c)
	{
		CrucyThread ct = new CrucyThread(c+"");
		lct.add(ct);
		ct.start();
		System.out.println("F");
		
		
	}
	
	public void printT(){
		for(CrucyThread t : lct){
			System.out.println(t.getName());
		}
	}
	
	
	public void printDB(){
		cdb.visualizeDB();
	}
	
	
	
	public synchronized void start() throws InterruptedException
	{
		System.out.println("====MANAGER CRUCY-DB===\n");
		Scanner scanner = new Scanner(System.in);
		String in = "";
		while(!in.equals("quit")){
		
			in = scanner.nextLine();
			switch(in){
			case "genera" : System.out.println("Inserisci un intero:");
				int c = Integer.parseInt(scanner.nextLine());
				generaT(c);
				break;
			
			case "print" :
				printDB();
				break;
				
			case "list" :
				printT();
				break;
				
			case "del" : System.out.println("Quale thread eliminare?");
				String th = scanner.nextLine();
				for(CrucyThread cc : lct){
					if(th.equals(cc.getName())){
						cc.suspend();
					}
				};break;
				
			case "resume" :System.out.println("Quale thread riavviare?");
				String thh = scanner.nextLine();
				for(CrucyThread cc : lct){
					if(thh.equals(cc.getName())){
						cc.resume();
					}
				};
				break;
			
			default: break;
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		CrucyGen cg = new CrucyGen();
		cg.start();
	}
	
	
}
