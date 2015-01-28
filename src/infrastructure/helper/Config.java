package infrastructure.helper;

import java.io.*;

import java.util.*;

import org.yaml.snakeyaml.Yaml;

public class Config {
	// A list of configurations (a single list format: [name, ip, port])
	private List<List> config;
	// Every single send rule is a map, where "action", "src", "dest"... are
	// keys
	private List<Map<String, Object>> sendRules;
	private List<Map<String, Object>> receiveRules; // Similar to sendRules

	public Config(String confFileName) {
		config = new ArrayList<List>();
		InputStream input = null;
		try {
			input = new FileInputStream(new File(confFileName));
			Yaml yaml = new Yaml();
			
			Map map = (Map) yaml.load(input);

			// parse configurations:
			List<Map<String, Object>> temp_list =
					(List<Map<String, Object>>) map.get("configuration");
			for (int i = 0; i < temp_list.size(); i++) {
				Map<String, Object> temp =
						(Map<String, Object>) temp_list.get(i);
				List conf = new ArrayList(); // [name, ip, port]
				conf.add(temp.get("name"));
				conf.add(temp.get("ip"));
				conf.add(temp.get("port"));
				config.add(conf);
			}
			
			// parse send rulse:
			sendRules = (List) map.get("sendRules");
			
			// parse receiveRulse:
			receiveRules = (List) map.get("receiveRules");
		}catch (FileNotFoundException e) {
			System.out.println("Rule initiation error");
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				System.out.println("file input closing error");
				e.printStackTrace();
			}
		}
	}
	
	public List<List> getConfig() {
		return config;
	}
	
	public List<Map<String, Object>> getSendRules() {
		return sendRules;
	}
	
	public List<Map<String, Object>> getReceiveRules() {
		return receiveRules;
	}

	public static void main(String[] argvs) {
		// test: 
		Config conf = new Config("file/test.yaml");
		
		System.out.println(conf.getConfig());
		System.out.println(conf.getSendRules());
		System.out.println(conf.getReceiveRules());
	}
}
