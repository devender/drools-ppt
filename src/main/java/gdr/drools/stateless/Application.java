package gdr.drools.stateless;


public class Application {
    private boolean valid;
    private int testScore;
    private String state;

    public Application(int testScore, String state){
    	this.testScore = testScore;
    	this.state = state;
    }
    
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public int getTestScore() {
		return testScore;
	}
    
	public String getState() {
		return state;
	}
    
}
