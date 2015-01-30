package infrastructure;

import infrastructure.config.Rule;
import infrastructure.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Connector {
	ConcurrentLinkedQueue<Message> delayedOutgoing, outgoing;
	ConcurrentLinkedQueue<Message> delayedIncoming;
	
	Object bufferLock = new Object() ;
	
	private List<Rule> sendRules, receiveRules;
	public Node des;
	public Socket socket;
	private MessagePasser mp; 
	
	public Connector(Node des, Socket socket, MessagePasser mp) {
		this.des = des ;
		this.socket = socket ;
		this.mp = mp ;
		this.sendRules = mp.getSendRuls() ;
		this.receiveRules = mp.getReceiveRules() ;
		
		outgoing = new ConcurrentLinkedQueue<Message>();
		delayedOutgoing = new ConcurrentLinkedQueue<Message>();
		delayedIncoming = new ConcurrentLinkedQueue<Message>();
		
		new Thread(sender).start();
		new Thread(receiver).start();
	}
	
	public void updateRules(){
		this.sendRules = mp.getSendRuls() ;
		this.receiveRules = mp.getReceiveRules() ;
	}
	
	public void send(Message msg){
		boolean shouldGo = true ;
		for(Rule r : this.sendRules){
			if (r.match(msg)){
				if (r.getAction().equals("drop")){
					shouldGo = false ;
				}else if (r.getAction().equals("duplicate")){
					Message newMsg = new Message(msg);
					newMsg.set_duplicate(true); 
					outgoing.add(newMsg);
					
					shouldGo = true ;
				}else if (r.getAction().equals("delay")){
					delayedOutgoing.add(msg);
					shouldGo = false ;
				}else{
					System.err.println("Matched a non recognized rule.");
					break ;
				}
			}
		}
		
		if ( shouldGo == false )
			return ;
		
		outgoing.add(msg);
		synchronized (bufferLock) {
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
							
							while(!delayedOutgoing.isEmpty()){
								Message msg = delayedOutgoing.poll();
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
							
							boolean shouldGo = true ; 
							
							for(Rule r : Connector.this.receiveRules){
								if (r.match(msg)){
									if (r.getAction().equals("drop")){
										shouldGo = false ;
										
										break ;
									}else if (r.getAction().equals("duplicate")){
										Message newMsg = new Message(msg);
										newMsg.set_duplicate(true); 
										mp.incoming.add(newMsg);
										shouldGo = true ;
										
										break ;
									}else if (r.getAction().equals("delay")){
										delayedIncoming.add(msg);
										shouldGo = false ;
										
										break ;
									}else{
										System.err.println("Matched a non recognized rule.");
										break ;
									}
								}
							}
							
							if ( shouldGo == false )
								continue ;
							
							while(!delayedIncoming.isEmpty()){
								Message delayedMsg = delayedIncoming.poll();
								mp.incoming.add(delayedMsg);
							}
							mp.incoming.add(msg);
						} catch (ClassNotFoundException e) {
						}
					}
				} catch (IOException e) {
					//TODO check if needed to remove
//					mp.connectors.remove(des.name); 
				}
		}			
	};
}
