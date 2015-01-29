package app ;

import java.io.IOException;
import java.util.Scanner;

import infrastructure.MessagePasser;
import infrastructure.message.Message;


public class Application{
	
   
   public static void main(String [] args){
	   try {
		Scanner cin = new Scanner(System.in);
		MessagePasser a = new MessagePasser("", "A");
		MessagePasser b = new MessagePasser("", "B");
		MessagePasser c = new MessagePasser("", "C");
		
		cin.next();
		try {
			a.send(new Message("B", "HELLO", "Hell no"));
			c.send(new Message("B", "HELLO", "Hello!!!!"));
			
			while(true){
				Message ret = b.receive();
				if (ret != null){
					System.out.println(ret + " : " + (String)ret.getPayload());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		cin.next();
//		MessagePasser d = new MessagePasser("", "D");
		cin.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
   }
}