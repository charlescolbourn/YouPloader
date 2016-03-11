package at.becast.youploader.youtube.data;

public class CategoryType {
	    private String value;
	    private String label;
	    
	    public CategoryType(String id, String label){
		    this.value = id;
		    this.label = label;
	    }
	    public String getValue(){
	    	return value;
	    }
	    public String toString(){
	    	return label;
	    }
}
