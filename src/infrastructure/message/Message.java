package infrastructure.message;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 9176640416276748875L;
	
	Header header ;
	Object payload ;
	
	
	public Message(String dest, String kind, Object data) {
	}
	
	// These settors are used by MessagePasser.send, not your app
	public void set_source(String source) {
	}
	public void set_seqNum(int sequenceNumber) {
	}
	public void set_duplicate(Boolean dupe) {
	}
	// other accessors, toString, etc as needed
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
