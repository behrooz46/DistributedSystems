package infrastructure.message;

public class Header {
	public String destination ; //name
	public int sequenceNumber ; //MP generates, unique between all messages sent by local node
	public boolean duplicate ; //Used by MP
	public String kind ;  //may not be unique
	public Header(String destination, int sequenceNumber, boolean duplicate, String kind) {
		super();
		this.destination = destination;
		this.sequenceNumber = sequenceNumber;
		this.duplicate = duplicate;
		this.kind = kind;
	}
}
