package emailclient;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

// The purose of this class 1) to display messeges with their content.
// 2) if user decides to save a message,create a subfolder in the specified path where the name of the folder will be subject of that message,and content is stored inside.
public class MessageProcessor {
    private String mainFolderPath;
    
    public MessageProcessor(String mainFolderPath){
        this.mainFolderPath = mainFolderPath;
    }
    
    // Iterating through messages displaying them and saving them if user wanted so.
    public void processMessages(Message[] messages){
        Arrays.stream(messages)
                .filter(this::toSave)
                .forEach(this::save);
    }
    
    // Displays some info about message to user and asks him if he would like to save it.
    private boolean toSave(Message message){
        try{
            String from = message.getFrom()[0].toString();
            String subject = message.getSubject();
            Date date = message.getReceivedDate();  // Working with POP3 protocol getReceivedDate is unsupported will return null.
            System.out.println(String.format("[From] : %3$S\n[Subject] : %1s\n[Received Date] : %2$tD",subject,date,from));
        }
        catch(MessagingException e){System.err.println("Message attribute is missing or malformed!");}
        return UserScanner.scanForSave();
    }
    
    // Saves the specified messages on local machine.
    private void save(Message message){
        try{
            // Creates a new sub-directory for each message.
            File folder = new File(mainFolderPath + "\\" + message.getSubject().trim());
            folder.mkdir();
            
            if(message.isMimeType("multipart/*")){
                Multipart multiPart = (Multipart)message.getContent();
                
                for(int i = 0;i < multiPart.getCount();i++){
                    MimeBodyPart bodyPart = (MimeBodyPart)multiPart.getBodyPart(i);
                    
                    if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) saveFile(bodyPart,folder);  // Checks whether the BodyPart is an attachment.
                    else if(bodyPart.isMimeType("text/plain")) saveText(bodyPart,folder);
                }
            }
        }
        catch(MessagingException | IOException  e){e.printStackTrace();}
        
    }
    // Saves attachments contained in the mesage.
    private void saveFile(MimeBodyPart bodyPart, File folder){
          try{bodyPart.saveFile(folder + "\\" + bodyPart.getFileName());}
          catch(MessagingException | IOException e){e.printStackTrace();}       
    }
    
    // Saves text contained in the messages.
    private void saveText(Part bodyPart,File folder){
   
        BufferedReader bufRead = null;
        BufferedWriter bufWrite = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        
        try{
            bufRead = new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
            bufWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder.getAbsolutePath() + "//" + dateFormat.format(new Date()) + ".txt")));
          
            String line;
            while((line = bufRead.readLine()) != null){
                bufWrite.write(line);
                bufWrite.newLine();
                bufWrite.flush();
            }                 
        }
        catch(MessagingException | IOException e){e.printStackTrace();}
        finally{
            if(bufRead != null){
                try{bufRead.close();}
                catch(IOException e){e.printStackTrace();}
            }
            if(bufWrite != null){
                try{bufWrite.close();}
                catch(IOException e){e.printStackTrace();}
            }
        }
    }
}
