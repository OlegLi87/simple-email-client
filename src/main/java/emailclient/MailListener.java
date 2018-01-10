package emailclient;

import com.sun.mail.imap.IMAPStore;
import javax.mail.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import static jdk.nashorn.internal.codegen.OptimisticTypesPersistence.store;

public class MailListener {
    private static Properties props = new Properties();
    private static Visualization visual;
    private static String mainFolderPath = "C:\\Users\\micro\\Desktop\\Messages";
       
    static void start(String host,String username,String password,Protocol protocol){
        // Fill Properties object.
        fillProperties(host);
        
        // Gets the Session object wrapped around properties and PasswordAuthentication objects.
        Session session = Session.getDefaultInstance(props,new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        });
        
        try{
            // Gets Store Objetct and try to establish a connection.
            Visualization.print(1,"Establishing connection with a remote server please wait");
            IMAPStore store = (IMAPStore)session.getStore(protocol.getProtocol());
            store.connect();   
            System.out.println("Connection successfuly established...");
            
            // Retrieves the folder and open it.
            Visualization.print(1,"Trying to retrieve the INBOX folder and open it");
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            System.out.println("Folder is successfuly retrieved and opened...");
            
            // Attaches event listener to the insatnce of that folder.
            attachFolderListener(folder);
            
            // Sends the messages to processing method.
            folderManagement(folder.getMessages());
            
            // Starts waiting(printing) process in the new Thread.
            visual = new Visualization();
            visual.start();
            
            // Constantly sends command to the server in order for MessageCountListener
            // to react on new messages if exist,until user types 'exit'.
            while(!Thread.currentThread().isInterrupted()){
                folder.getMessageCount(); //  Each one minute sends request to the remote server to check for new messages.
                try{TimeUnit.SECONDS.sleep(60);}  
                catch(InterruptedException e){Thread.currentThread().interrupt();}          
            }          
            folder.close(true);
            store.close();
            System.exit(0);
        }
        catch(MessagingException e){e.printStackTrace();}
    }
    
    private static void attachFolderListener(Folder folder){
        folder.addMessageCountListener(new MessageCountListener(){
            @Override
            public void messagesAdded(MessageCountEvent e){              
                visual.interrupt();    // Interrupts the Thread that printing and scanning user's input to make it terminate.
                while(visual.isAlive()){} // Wait in case scanning Thread is blocked by scanner.nextLine().
                folderManagement(e.getMessages());
                visual = new Visualization();
                visual.start();
            }
            @Override
            public void messagesRemoved(MessageCountEvent e){
            }
        });
    }
    
    private static void folderManagement(Message[] messages){
        // At the start of the program there may be no new messages.
        if(messages.length > 0){
            if(UserScanner.scanForYes(messages.length).equalsIgnoreCase("yes")){
               MessageProcessor processor = new MessageProcessor(mainFolderPath);
               processor.processMessages(messages);
            }
        }
        else System.out.println("There are no new messages!");
    }
    
    // Filling the Properties object with properties accordingly to host (protocol) type.
    private static void fillProperties(String host){
        if(host.contains("imap")){
            props.put("mail.imaps.host", host);
            props.put("mail.imaps.port","993");
        }
        else if(host.contains("pop")){
            props.put("mail.pop3s.host",host);
            props.put("mail.pop3s.port","995");
        }
        else{
            System.err.println("The provided host type is un-recognized !!!");
            System.err.println("The program will be terminated !!!");
            System.exit(0);
        }
    }
}
