
public class CrucyDef {

	private int diff;
	private long index;
	private Type type;
	
	public enum Type { IMG, TXT};

	
	public CrucyDef(int diff, long index, Type type){
		this.diff = diff;
		this.index = index;
		this.type = type;
	}
	
	public int getDifficoltà(){
		return diff;
	}
	
	public long getIndex(){
		return index;
	}
	
	public String getType(){
		return type.toString();
	}
	
	public String toString(){
		String s="";
		s+="Diff="+this.diff+"-Indx="+this.index;
		return s;
	}
	
}
