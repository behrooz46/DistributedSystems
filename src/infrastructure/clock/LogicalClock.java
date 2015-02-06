package infrastructure.clock;

import infrastructure.clock.exception.UndecidableException;

public class LogicalClock implements Clock {

	@Override
	public boolean isConcurrent(Clock c) throws UndecidableException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasHappenedBefore(Clock c) throws UndecidableException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Clock increment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clock increment(Clock c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clock clone() {
		//TODO new LogicalClock() ;
		return null;
	}

	@Override
	public Clock parseTimestamp(String timestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

}
