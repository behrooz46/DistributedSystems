package infrastructure;

import infrastructure.connection.Listener;
import infrastructure.connection.Speaker;
import infrastructure.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;


public class MessagePasser{
	public String nodeName;
	public String confFileName;
	public int seqID ;
	
	public ServerSocket serverSocket;
	public ConcurrentLinkedQueue<Message> incoming ;
	public Map<String, Connector> connectors ; 
	public Map<String, Node> nodes ;
	
	public Object lock = new Object();
	
	public MessagePasser(String configurationFilename, String localName) throws IOException {
		this.nodeName = localName ;
		this.confFileName = configurationFilename ;
		this.seqID = 0 ;
		
		this.nodes = new HashMap<String, Node>() ;
		this.connectors = new HashMap<String, Connector>() ;
		incoming = new ConcurrentLinkedQueue<Message>();
		
		initialize() ;
	}

	private void initialize() throws IOException {
		//TODO Config config = new Config(confFileName);
		this.nodes.put("A", new Node("A", "localhost", 4411));
		this.nodes.put("B", new Node("B", "localhost", 5511));
		this.nodes.put("C", new Node("C", "localhost", 6661));
		this.nodes.put("D", new Node("D", "localhost", 7771));
		//-------------------------
		Node me = this.nodes.get(this.nodeName) ;
		//-------------------------
		new Listener(this, me.port).start();
		new Speaker(this).start();
	}
	

	public void send(Message message) throws Exception {
		String des = message.getHeader().destination ; 
		if (connectors.containsKey(des) == false || connectors.get(des) == null){
			throw new Exception("Host is not connected!");
		}
		
		Connector conn = connectors.get(des);
		message.set_seqNum(this.seqID++);
		message.set_source(this.nodeName);
		conn.send(message);
	}
	
	
	//TODO if config file changed, notify all connectors 

	public Message receive( ) {
		// may block. Doesn't have to.
		// TODO lock 
		return incoming.poll() ;
	} 
}
