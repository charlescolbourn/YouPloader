package at.becast.youploader.account;

public class AccountType {
	    private int value;
	    private String label;
	    
	    public AccountType(int id, String label){
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
