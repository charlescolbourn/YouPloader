package at.becast.youploader.youtube.data;

public class CategoryType {
	    private int value;
	    private String label;
	    
	    public CategoryType(int id, String label){
		    this.value = id;
		    this.label = label;
	    }
	    public int getValue(){
	    	return value;
	    }
	    public String toString(){
	    	return label;
	    }
}
