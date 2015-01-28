package infrastructure;

import infrastructure.message.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MessagePasser{
	private String nodeName;
	private String confFileName;
	private int seqID ;
	
	private ServerSocket serverSocket;
	Buffer incoming ;
	Map<String, Connector> connectors ; 
	Map<String, Node> nodes ;
	

	public MessagePasser(String configurationFilename, String localName) throws IOException {
		this.nodeName = localName ;
		this.confFileName = configurationFilename ;
		this.seqID = 0 ;
		
		this.nodes = new HashMap<String, Node>() ;
		this.connectors = new HashMap<String, Connector>() ;
		
		initialize() ;
	}

	private void initialize() throws IOException {
		//TODO Config config = new Config(confFileName);
		//TODO read details of config 
		this.nodes.put("A", new Node("A", "localhost", 4444));
		this.nodes.put("B", new Node("B", "localhost", 5555));
		
		Node me = this.nodes.get(this.nodeName) ;
		this.serverSocket = new ServerSocket(me.port) ;
		
		
		Runnable listner = new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try{
						MessagePasser self = MessagePasser.this ;
						
						System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
						Socket socket = serverSocket.accept();
						System.out.println("Just connected to " + socket.getRemoteSocketAddress());

						DataInputStream in = new DataInputStream(socket.getInputStream());
						String nodeName = in.readUTF() ;
						System.out.println(nodeName);
						
						DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				        out.writeUTF(self.nodeName);
						
						if (connectors.containsKey(nodeName) == false){
							//New Connection
							Connector conn = new Connector(self.nodes.get(nodeName), socket, self, null);
							connectors.put(nodeName, conn) ;
							conn.start() ;
						}else if (connectors.get(nodeName) == null){
							//I'm trying
							socket.close();
						}else{
							//Already there's a connection
							socket.close();
						}
					}catch(SocketTimeoutException s){
						System.out.println("Socket timed out!");
						break;
					}catch(IOException e){
						e.printStackTrace();
						break;
					}
				}
				
			}
		};
		
		Runnable connector = new Runnable() {
			@Override
			public void run() {
				Random random = new Random();
				while(true){
					MessagePasser self = MessagePasser.this ;
					
					for (String nodeName : self.nodes.keySet()) {
						if (nodeName.equals(self.nodeName))
							continue ;
						if (self.connectors.containsKey(nodeName))
							continue ;
						
						Node node = self.nodes.get(nodeName);
						self.connectors.put(nodeName, null);
						
						try{
							Socket socket = new Socket(node.ip, node.port);
					        System.out.println("Just connected to " + socket.getRemoteSocketAddress());
					        
					        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					        out.writeUTF(self.nodeName);
					        
					        DataInputStream in = new DataInputStream(socket.getInputStream());
					        System.out.println("Server says " + in.readUTF());
					        
					        Connector conn = new Connector(self.nodes.get(nodeName), socket, self, null);
							connectors.put(nodeName, conn) ;
							conn.start() ;
						}catch(SocketTimeoutException s){
							System.out.println("Socket timed out!");
							
							self.connectors.remove(nodeName);
							break;
						}catch(IOException e){
							e.printStackTrace();
							
							self.connectors.remove(nodeName);
							break;
						}
					}
					
					try {
						int timeToSleep = random.nextInt(5) * 1000 ;
						Thread.sleep(timeToSleep) ;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		new Thread(listner).start();
		new Thread(connector).start();
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
