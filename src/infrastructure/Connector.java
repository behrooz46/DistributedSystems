package infrastructure;

import infrastructure.helper.Rule;
import infrastructure.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Connector {
	ConcurrentLinkedQueue<Message> delayedQueue, outgoing;
	Object bufferLock = new Object() ;
	
	private ArrayList<Rule> rules;
	public Node des;
	public Socket socket;
	private MessagePasser mp; 
	
	public Connector(Node des, Socket socket, MessagePasser mp, ArrayList<Rule> rules) {
		//TODO create two thread for sending / receiving 
		//TODO mp.getIncoming 
		this.des = des ;
		this.socket = socket ;
		this.mp = mp ;
		this.rules = new ArrayList<Rule>() ;
		outgoing = new ConcurrentLinkedQueue<Message>();
		delayedQueue = new ConcurrentLinkedQueue<Message>();
		
		new Thread(sender).start();
		new Thread(receiver).start();
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
		synchronized (bufferLock) {
			outgoing.add(msg);
			bufferLock.notify();
		}
	} 
	
	Runnable sender = new Runnable() {
		@Override
		public void run() {
			try {
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				while(true){
//					System.err.println("Waiting for send Lock");
					synchronized (bufferLock) {
						try {
							bufferLock.wait();
//							System.err.println("Got send Lock");
							
							
							while(!outgoing.isEmpty()){
								Message msg = outgoing.poll();
								out.writeObject(msg);
							}
							
							while(!delayedQueue.isEmpty()){
								Message msg = delayedQueue.poll();
								out.writeObject(msg);
							}
							out.flush();
						} catch (InterruptedException e) {
						}
					}
				}
			}catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	};
	
	Runnable receiver = new Runnable() {
		@Override
		public void run() {
				try {
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					while(true){
						try {
							Message msg = (Message)in.readObject();
							synchronized (mp.incoming) {
								mp.incoming.add(msg);
							}
						} catch (ClassNotFoundException e) {
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}			
	};
}
