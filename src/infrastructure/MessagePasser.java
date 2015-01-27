package infrastructure;

import infrastructure.helper.Config;
import infrastructure.message.Message;


public class MessagePasser {
	private String nodeName;
	private String confFileName;
	private int seqID ;
	
	Buffer incoming, delayedOutgoing, outgoing ;

	public MessagePasser(String configurationFilename, String localName) {
		this.nodeName = localName ;
		this.confFileName = configurationFilename ;
		this.seqID = 0 ;
		
		initialize() ; 
	}

	private void initialize() {
		Config config = new Config(confFileName);
		//TODO
	}

	void send(Message message) {
		//set the sequence number ... non-reused, strictly incrementing integer from 0 
		
		//update rules
		//check against rules
		//put into queue
	}
	
	//real send function
	//when we have sth to send 
	//send everything from both outgoing & delayedOutgoing
	 
	//real receive function
	//check against rules
	//store into buffer
	 
	Message receive( ) {
		// may block. Doesn't have to.
		// read buffer.poll()
		return null;
	} 
}
