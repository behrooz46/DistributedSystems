package app ;

import infrastructure.MessagePasser;
import infrastructure.message.Message;

import java.io.IOException;
import java.util.Scanner;


public class Application{
	
   
   public static void main(String [] args){
	   try {
		Scanner cin = new Scanner(System.in);
		System.out.println("When in doubt shout for \"help\" !");
		System.out.print("Enter node name: ");
		
		String name = cin.next();
		MessagePasser mp = new MessagePasser("file/test.yaml", name);
		
		
		while(true){
			System.out.print("I am at your command: ");
			String cmd = cin.next() ;
			if (cmd.equals("help")){
				System.out.println("Commands available: [exit / help / whoami / send node kind data / get]");
			}else if (cmd.equals("exit")){
				System.out.println("Bye bye!");
				break ;
			}else if (cmd.equals("send")){
				String dest = cin.next() ;
				String kind = cin.next() ;
				String data = cin.next() ;
				try {
					mp.send(new Message(dest, kind, data));
				} catch (Exception e) {
					System.out.println("ERROR: " + e.getMessage());
				}
			}else if (cmd.equals("get")){
				Message msg = mp.receive();
				if (msg == null)
					System.out.println("ERROR: no incoming msg.");
				else
					System.out.println(msg + " " + (String)msg.getPayload());
			}else if (cmd.equals("whoami")){
				System.out.println("You are \""+ name +"\"");
			}else{
				System.out.println("Command not recognized.");
			}
		}
		
		cin.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	System.exit(0);
   }
}