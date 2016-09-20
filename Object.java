import java.io.Serializable;

public class Object implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String s;
	
	public Object(String s){
		this.s = s;
	}
	
	public String getS(){return s;}
	
}
