package infrastructure.handshake;

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
				Socket socket = serverSocket.accept();
					
				DataInputStream in = new DataInputStream(socket.getInputStream());
				String nodeName = in.readUTF() ;
				
		        synchronized (self.lock) {
		        	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		        	
					if (self.connectors.containsKey(nodeName) == false){
						Connector conn = new Connector(self.nodes.get(nodeName), socket, self);
						self.connectors.put(nodeName, conn) ;
						System.err.println("** CONNECTION ** (" + nodeName +  ", " + self.nodeName + ")");

						out.writeUTF(self.nodeName);
					}else if (self.connectors.get(nodeName) == null){
						System.err.println(self.nodeName + " trying to connect to " + nodeName);
						out.writeUTF("");
						socket.close();
					}else{
						System.err.println(self.nodeName + " there's connection to " + nodeName);
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
