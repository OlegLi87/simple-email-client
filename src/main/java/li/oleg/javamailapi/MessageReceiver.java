package li.oleg.javamailapi;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import com.sun.mail.pop3.POP3Store;
import java.io.*;

public class MessageReceiver {
    
    MessageReceiver(){
        collectMail();
    }
    
    private void collectMail(){
        //Getting Session Object
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);
        
        try{
            String storeType = "pop3s";
            POP3Store popStore = (POP3Store)session.getStore(storeType);
            popStore.connect("pop.gmail.com","8HugoStiglitz7","nameja1666");
            
            Folder folder = popStore.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            
            Message[] messages = folder.getMessages();
            
            System.out.println(messages.length);
                     
           folder.close(false);
            popStore.close();
        }
        catch(Exception e) {System.err.println(e.getMessage());}
    }
    
}

