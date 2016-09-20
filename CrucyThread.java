/**
 * 
 * CrucyThread è un safe thread che comincia la generazione
 * di un cruciverba con un determinato schema, dominio e difficoltà
 *
 */
public class CrucyThread implements Runnable{

	private Thread t;
	private String name;
	private volatile boolean suspend;
	
	public CrucyThread(String name) // <- parametri del Builder
	{
		this.name = name;
	}
	
	@Override
	public void run() {	// <- parametri builder e build()
		//Cruciverba Builder
	}
	
	public void start(){
		if ( t == null){
			t = new Thread(this,name);
			t.start();
		}
	}
	
	public synchronized void suspend(){
		suspend = true;
	}
	
	public synchronized void resume(){
		suspend = false;
		notify();
	}
	
	public String getName(){return name;}

}
