package infrastructure.config;

import infrastructure.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Config {
	private List<Rule> sendRules, receiveRules;
	private ArrayList<Node> nodes;

	public Config(String confFileName) throws FileNotFoundException {
		this.nodes = new ArrayList<Node> () ;
		this.sendRules = new ArrayList<Rule> () ;
		this.receiveRules = new ArrayList<Rule> () ;
		
		InputStream input = new FileInputStream(new File(confFileName));
		Yaml yaml = new Yaml();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) yaml.load(input);
		
		@SuppressWarnings("unchecked")
		List< Map<String, Object> > conf = (List< Map<String, Object> >) map.get("configuration");
		for(Map<String, Object> entry : conf){
			Node node = new Node((String)entry.get("name"), (String)entry.get("ip"), (Integer)entry.get("port"));
			this.nodes.add(node);
		}
			
		@SuppressWarnings("unchecked")
		List< Map<String, Object> > send = (List< Map<String, Object> >) map.get("sendRules");
		for(Map<String, Object> entry : send){
			Rule rule = new Rule(entry);
			this.sendRules.add(rule);
		}
		
		@SuppressWarnings("unchecked")
		List< Map<String, Object> > recieve = (List< Map<String, Object> >) map.get("receiveRules");
		for(Map<String, Object> entry : send){
			Rule rule = new Rule(entry);
			this.receiveRules.add(rule);
		}
	}
	
	public List<Rule> getSendRules() {
		return sendRules;
	}
	
	public List<Rule> getReceiveRules() {
		return receiveRules;
	}

//	public static void main(String[] argvs) throws FileNotFoundException {
//		// test: 
//		Config conf = new Config("file/test.yaml");
//		
//		System.out.println(conf.getNodes());
//		System.out.println(conf.getSendRules());
//		System.out.println(conf.getReceiveRules());
//	}

	public ArrayList<Node> getNodes() {
		return nodes ; 
	}
}
