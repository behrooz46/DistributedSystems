package infrastructure.message;

import java.io.Serializable;

public class Header implements Serializable {
	private static final long serialVersionUID = -3826953472807448125L;
	
	public String destination ; //name
	public int sequenceNumber ; //MP generates, unique between all messages sent by local node
	public boolean duplicate ; //Used by MP
	public String kind ;  //may not be unique
	public String source;
	
	public Header(String destination, int sequenceNumber, boolean duplicate, String kind) {
		super();
		this.destination = destination;
		this.sequenceNumber = sequenceNumber;
		this.duplicate = duplicate;
		this.kind = kind;
	}
	
	@Override
	public String toString() {
		return source + " " + destination + ", #" + sequenceNumber + ( duplicate ? ", DUP, " : ", " ) + kind ;  
	}
}
