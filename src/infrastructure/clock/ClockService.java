package infrastructure.clock;


public class ClockService {
	public static final int LOGICAL = 0, VECTOR = 1;
	
	private static ClockService instance ;
	private int type;
	private Clock clock;
	
	private ClockService(){
		//read yaml file
		this.type = LOGICAL ;
		
		if (this.type == LOGICAL){
			this.type = LOGICAL ;
			this.clock = new LogicalClock();
		}else{
			this.type = VECTOR ;
			this.clock = new VectorClock();
		}
	}
	
	/**
	 * By default generates a logical clock, unless otherwise 
	 * explicitly set with TYPE value before first instantiation. 
	 * @return Clock Manager Instance
	 */
	public static ClockService getInstance(){
		if (instance == null){
			instance = new ClockService();
		}
		return instance ;
	}
	
	
	public String timestampEvent(){
		clock.increment();
		return clock.getTimestamp();
	}
	
	public String timestampSend(){
		clock.increment();
		return clock.getTimestamp() ;
	}
	
	public String timestampRecieve(String timestamp){
		Clock c = clock.parseTimestamp(timestamp);
		clock.increment(c);
		return clock.getTimestamp();
	}
	
	public Clock getCurrentClock(){
		return clock.clone() ;
	}
	
	public Clock parseTimestamp(String timestamp){
		return clock.parseTimestamp(timestamp);
	}
	
	public String getTimestamp(Clock c){
		return c.getTimestamp();
	}
	
}
