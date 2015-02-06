package infrastructure.clock;

import infrastructure.clock.exception.UndecidableException;

public interface Clock {
	public boolean isConcurrent(Clock c) throws UndecidableException;
	public boolean hasHappenedBefore(Clock c) throws UndecidableException;
	
	public Clock increment();
	public Clock increment(Clock c);
	
	public Clock clone();
	
	public Clock parseTimestamp(String timestamp);
	public String getTimestamp();
}
