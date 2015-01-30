package infrastructure;

import infrastructure.config.Config;
import infrastructure.config.Rule;
import infrastructure.handshake.Listener;
import infrastructure.handshake.Speaker;
import infrastructure.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private List<Rule> sendRules;
	private List<Rule> receiveRules;
	
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
		Config config = new Config(confFileName);
		for(Node node : config.getNodes()){
			this.nodes.put(node.name, node);
		}
		this.sendRules = config.getSendRules();
		this.receiveRules = config.getReceiveRules();
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
		
		//TODO if config file changed, notify all connectors
		
		Connector conn = connectors.get(des);
		message.set_seqNum(this.seqID++);
		message.set_source(this.nodeName);
		conn.send(message);
	}

	public Message receive( ) {
		return incoming.poll() ;
	}
	
	public List<Rule> getSendRuls(){
		return sendRules;
	}
	
	public List<Rule> getReceiveRules(){
		return receiveRules;
	}
}
