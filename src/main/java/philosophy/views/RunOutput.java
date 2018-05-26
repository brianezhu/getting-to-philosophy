package philosophy.views;

import java.util.ArrayList;

public class RunOutput {
    private final String completed;
    private final String status;
    private final int numOfHops;
    private final ArrayList<String> path;

    public RunOutput(String completed, String status, ArrayList<String> links) {
        this.completed = completed;
        this.status = status;
        this.numOfHops = links.size();
        this.path = links;
    }
    
    public RunOutput(String completed, String status) {
    	this.completed = completed;
    	this.status = status;
    	this.numOfHops = -1;
    	this.path = null;
    }

    public String getCompleted() {
        return completed;
    }

    public String getStatus() {
        return status;
    }

    public int getNumOfHops() {
        return numOfHops;
    }
    
    public ArrayList<String> getPath() {
    	return path;
    }
}
