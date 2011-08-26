package gdr.drools.stateful;

import java.util.ArrayList;
import java.util.List;


public class Status {
	private List<String> messages =new ArrayList<String>();
	
	public void add(String mgs){
		messages.add(mgs);
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
}
