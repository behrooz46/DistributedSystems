package infrastructure.config;

import java.util.Map;

import infrastructure.message.Message;

public class Rule {
	private Map<String, Object> map;

	public Rule(Map<String, Object> entry) {
		this.map = entry ;
	}
	
	public String getAction(){
		return (String)map.get("action");
	}

	public boolean match(Message msg) {
		if (map.containsKey("src")){
			if (! msg.getHeader().source.equals((String)map.get("src")) )
				return false ;
		}
		if (map.containsKey("dest")){
			if (! msg.getHeader().destination.equals((String)map.get("dest")) )
				return false ;
		}
		if (map.containsKey("kind")){
			if (! msg.getHeader().kind.equals((String)map.get("kind")) )
				return false ;
		}
		if (map.containsKey("duplicate")){
			//FIXME check duplicate works 
			if ( map.get("duplicate").equals("True") && msg.getHeader().duplicate == true )
				return false ;
			if ( map.get("duplicate").equals("False") && msg.getHeader().duplicate == false )
				return false ;
		}
		if (map.containsKey("seqNum")){
			int seqNum = (Integer)(map.get("seqNum"));
			if (seqNum != msg.getHeader().sequenceNumber)
				return false ;
		}
		return true;
	}

}
