package infrastructure.message;

public class Header {
	public String destination ; //name
	public int sequenceNumber ; //MP generates, unique between all messages sent by local node
	public boolean duplicate ; //Used by MP
	public String kind ;  //may not be unique
}
