package infrastructure;

import infrastructure.helper.Rule;
import infrastructure.message.Message;

import java.net.Socket;
import java.util.ArrayList;

public class Connector extends Thread {
	Buffer delayedQueue, outgoing;
	private ArrayList<Rule> rules; 
	
	public Connector(Node des, Socket socket, MessagePasser mp, ArrayList<Rule> rules) {
		//TODO create two thread for sending / receiving 
		//TODO mp.getIncoming 
	}
	
	public void updateRules(ArrayList<Rule> rules){
		this.rules = rules ;
	}
	
	public void send(Message msg){
		for(Rule r : rules){
			if (r.match(msg)){
				//TODO decide what to do - first matched
				
				return ;
			}
		}
		//TODO decide what to do - non matched
	} 
	
	//real send function
	//when we have sth to send 
	//send everything from both outgoing & delayedOutgoing
	
	//real receive function
	//check against rules
	//store into buffer
}
