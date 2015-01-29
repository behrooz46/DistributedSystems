package infrastructure.connection;

import infrastructure.Connector;
import infrastructure.MessagePasser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Listener extends Thread {
	private MessagePasser self;
	private ServerSocket serverSocket;
	public Listener(MessagePasser self, int port) throws IOException {
		this.self = self ;
		this.serverSocket = new ServerSocket(port) ;
	}
	@Override
	public void run() {
		while(true){
			try{
//				System.out.println("Waiting for client on port " + self.serverSocket.getLocalPort() + "...");
//				System.err.println("Listner Before Server Socket - " + self.nodeName);
				Socket socket = serverSocket.accept();
//				System.out.println("Just connected to " + socket.getRemoteSocketAddress());
//				System.err.println("Listner Before Lock - " + self.nodeName);
				
					
				DataInputStream in = new DataInputStream(socket.getInputStream());
				String nodeName = in.readUTF() ;
//				System.out.println("Connectd to " + self.nodeName +  " says: " + nodeName);
				
				
		        
				
		        synchronized (self.lock) {
		        	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		        	
//		        	System.err.println("Got the Lock - " + self.nodeName);
					if (self.connectors.containsKey(nodeName) == false){
						//New Connection
						Connector conn = new Connector(self.nodes.get(nodeName), socket, self, null);
						self.connectors.put(nodeName, conn) ;
						System.out.println("** CONNECTION ** (" + nodeName +  ", " + self.nodeName + ")");

						out.writeUTF(self.nodeName);
					}else if (self.connectors.get(nodeName) == null){
						System.out.println(self.nodeName + " trying to connect to " + nodeName);
						//I'm trying
						out.writeUTF("");
						socket.close();
					}else{
						System.out.println(self.nodeName + " there's connection to " + nodeName);
						//Already there's a connection
						out.writeUTF("");
						socket.close();
					}
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
}
