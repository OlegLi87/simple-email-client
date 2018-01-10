package li.oleg.javamailapi;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.stream.Stream;

public class MessageSender {
    private DataReader reader;
    private Session session;
    private List<MimeMessage> messages;  // Equals to amount of recipients from the file.
    
    MessageSender(DataReader reader){
        this.reader = reader;
        createSession();
        
        messages = new ArrayList<>();
        sendMessage();
    }
    
    //Creating Session object.
    private void createSession(){
        Properties props = new Properties(); 
        props.putAll(reader.getProps());      
             
        String username,password;
        
        Set<Map.Entry<String,String>> entries = reader.getHosts().entrySet();
        Iterator<Map.Entry<String,String>> iteratorHosts = entries.iterator();
        Map.Entry<String,String> entry = iteratorHosts.next();
        
        username = entry.getKey();
        password = entry.getValue();
              
        session = Session.getDefaultInstance(props,new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        });
    }
    
    //Creating message instances and adding them to the list.
    private void sendMessage(){
        popRecipient();
        
        if(messages.size() > 0){
            try{
                for(MimeMessage msg : messages){
                    Transport.send(msg);
                }
            }
            catch(MessagingException e){e.printStackTrace();}
        }
    }
    
    //Take out each recipient adress and forward it to createMessage method.
    private void popRecipient(){
        Stream<String> recipients = reader.getRecipients().stream();
        recipients.forEach(this::createMessage);
    }
    
    //Create message instance of specified recipient's adress.
    private void createMessage(String recipient){
        String msgText = reader.getMessage();
        MimeMessage message = new MimeMessage(session);
        
        try{
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Hi");
            message.setText(msgText);
            
            messages.add(message);
        }
        catch(MessagingException e){e.printStackTrace();}
    }
}
