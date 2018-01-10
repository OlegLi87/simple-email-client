package li.oleg.javamailapi;

import java.io.*;

public class TesterClass {
    
    private static File propsDest = new File("C:\\Users\\micro\\Desktop\\MailAPI\\Properties.txt");
    private static File authDest = new File("C:\\Users\\micro\\Desktop\\MailAPI\\Authentification.txt");
    private static File msgDest = new File("C:\\Users\\micro\\Desktop\\MailAPI\\Message.txt");
    private static File recipientDest = new File("C:\\Users\\micro\\Desktop\\MailAPI\\Emails.txt");
    
    public static void main(String[] args){
        DataReader reader = new DataReader(propsDest,authDest,recipientDest,msgDest);
          
        //Sending Message
       // MessageSender sender = new MessageSender(reader);
       
       MessageReceiver receiver = new MessageReceiver();
    }
}
