package infrastructure.message;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 9176640416276748875L;
	
	Header header ;
	Object payload ;
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Header getHeader() {
		return header;
	}

	public Object getPayload() {
		return payload;
	}
	
	public Message(String dest, String kind, Object data) {
		header = new Header(dest, 0, false, kind);
		this.payload = data ;
	}
	
	public Message(Message msg) {
		this.header = new Header(msg.getHeader());
		this.payload = msg.getPayload() ;
	}

	public void set_source(String source) {
		header.source = source ;
	}
	public void set_seqNum(int sequenceNumber) {
		header.sequenceNumber = sequenceNumber ;
	}
	public void set_duplicate(Boolean dupe) {
		header.duplicate = true ;
	}
	@Override
	public String toString() {
		return "[" + header.toString() + "]" ; 
	}
}
