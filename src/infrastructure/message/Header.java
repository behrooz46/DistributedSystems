package infrastructure.message;

public class Header {
	String destination ; //name
	int sequenceNumber ; //MP generates, unique between all messages sent by local node
	boolean duplicate ; //Used by MP
	String kind ;  //may not be unique
}
