package infrastructure;

import java.util.Map;

import infrastructure.helper.Config;
import infrastructure.message.Message;


public class MessagePasser {
	private String nodeName;
	private String confFileName;
	private int seqID ;
	
	Buffer incoming ;
	Map<String, Connector> connectors ; 

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
		String des = message.getHeader().destination ; 
		if (connectors.containsKey(des) == false){
			//TODO connect with socket :D;
		}
		
		Connector conn = connectors.get(des);
		message.set_seqNum(this.seqID++);
		message.set_source(this.nodeName);
		conn.send(message);
	}
	
	
	//TODO if config file changed, notify all connectors 

	Message receive( ) {
		// may block. Doesn't have to.
		// TODO lock 
		return incoming.poll() ;
	} 
}
