package infrastructure.connection;

import infrastructure.Connector;
import infrastructure.MessagePasser;
import infrastructure.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

public class Speaker extends Thread {
	private MessagePasser self;
	public Speaker(MessagePasser self) {
		this.self = self ;
	}
	@Override
	public void run() {
		Random random = new Random();
		while(true){
			
			for (String nodeName : self.nodes.keySet()) {
				if (nodeName.equals(self.nodeName))
					continue ;
				
				if (self.connectors.containsKey(nodeName))
					continue ;
				
//				System.err.println("Speaker Before Lock - " + self.nodeName);
				
//				System.err.println("Speaker After Lock - " + self.nodeName);
				Node node = self.nodes.get(nodeName);
				self.connectors.put(nodeName, null);
				
				try{
//					System.err.println("Speaking to " + node.name +" from " + self.nodeName);
					Socket socket = new Socket(node.ip, node.port);
//				        System.out.println("Just connected to " + socket.getRemoteSocketAddress());
			        
			        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			        out.writeUTF(self.nodeName);
			        
			        DataInputStream in = new DataInputStream(socket.getInputStream());
			        String desName = in.readUTF();
//			        System.out.println("Connectd to " + self.nodeName +  " says: " + desName);
			        
			        if (desName.equals(nodeName)){
				        synchronized (self.lock) {
					        Connector conn = new Connector(self.nodes.get(nodeName), socket, self, null);
					        System.out.println("** CONNECTION ** (" + self.nodeName +  ", " + nodeName + ")");
							self.connectors.put(nodeName, conn) ;
				        }
			        }else{
			        	synchronized (self.lock) {
							self.connectors.remove(nodeName);
						}
			        }
					
				}catch(SocketTimeoutException s){
					synchronized (self.lock) {
						self.connectors.remove(nodeName);
					}
				}catch(IOException e){
					synchronized (self.lock) {
						self.connectors.remove(nodeName);
					}
				}
			}
			
			try {
				int timeToSleep = random.nextInt(5) * 100 ;
				Thread.sleep(timeToSleep) ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}