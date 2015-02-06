package infrastructure.message;



public class TimestampedMessage extends Message {
	private static final long serialVersionUID = -2084893066683100858L;
	
	public String timestamp;
	public TimestampedMessage(Message msg, String timestamp) {
		super(msg);
		this.timestamp = timestamp ;
	}
	public TimestampedMessage(TimestampedMessage msg) {
		super(msg);
		this.timestamp = msg.timestamp ;
	}
}
