package li.oleg.javamailapi;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class DataReader {
    private Map<String,String> propsMap;
    private Map<String,String> hostAuthentication;
    private List<String> recipients;
    private String message;
    
    DataReader(File propsDest,File authDest,File recipientDest,File msgDest){
        propsMap = new LinkedHashMap<>();
        fillProperties(propsDest);
        
        hostAuthentication = new HashMap<>();
        fillAuthentication(authDest);
        
        recipients = new ArrayList<>();
        fillRecipients(recipientDest);
        
        getMessage(msgDest);
    }
    
    //Filling propsMap with properties from the file.
    private void fillProperties(File propsDest){
        List<String> props = new LinkedList<>();
        
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(propsDest),"UTF-8"))){
            String line;
            
            while((line = br.readLine()) != null){
                props.add(line);              
            }
        }
        catch(IOException e){e.printStackTrace();}
                   
        try{
            if(props.size() > 0){
                Stream<String> stream = props.stream();
                List<String> keys = new LinkedList<>();
                
                keys = stream.filter(s -> s.contains(","))
                        .map(s -> s.substring(0,s.indexOf(",")))
                        .collect(Collectors.toList());
                                     
                stream = props.stream();
                List<String> values = new LinkedList<>();
                
                values = stream.filter(s -> s.contains(","))
                        .map(s -> s.substring(s.indexOf(',') + 1))
                        .collect(Collectors.toList());
                 
                int min = Arrays.asList(keys,values).stream()
                        .mapToInt(l -> l.size())
                        .min()
                        .getAsInt();
                
                for(int i = 0;i < min;i++){
                    propsMap.put(keys.get(i),values.get(i));
                }            
            }
            else throw new EmptyFileException(propsDest);
            
        }
        catch(EmptyFileException e){System.err.println(e.getMessage());}
        
    }
    
    //Filing hostAuthentication with values from the file.
    private void fillAuthentication(File authDest){
        List<String> hosts = new LinkedList<>();
        List<String> passwords = new LinkedList<>();
        
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(authDest),"UTF-8"))){
            String line;
            
            while((line = br.readLine()) != null){
                if(line.contains("username")) hosts.add(line.substring(line.indexOf('=') + 1));
                else passwords.add(line.substring(line.indexOf('=') + 1));
            }
        }
        catch(IOException e){e.printStackTrace();}
        
        try{
            if(hosts.size() > 0 && passwords.size() > 0){
                
                int min = Arrays.asList(hosts,passwords).stream()
                        .mapToInt(l -> l.size())
                        .min()
                        .getAsInt();
                
                for(int i = 0;i < min;i++){
                    hostAuthentication.put(hosts.get(i), passwords.get(i));
                }
            }
            else throw new EmptyFileException(authDest);
        }
        catch(EmptyFileException e){System.err.println(e.getMessage());}
    }
    
    //Filling list of recipients with values from the file.
    private void fillRecipients(File recipientDest){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(recipientDest),"UTF-8"))){
            String line;
            
            while((line = br.readLine()) != null){
                if(line.contains("@"))recipients.add(line);
            }
            if(!(recipients.size() > 0)) throw new EmptyFileException(recipientDest);
        }
        catch(IOException | EmptyFileException e){System.err.println(e.getMessage());}
    }
    
    //Reading the message from the file.
    private void getMessage(File msgDest){
        try(InputStreamReader in = new InputStreamReader(new FileInputStream(msgDest),"UTF-8")){
            
            StringBuffer sbf = new StringBuffer();
            int ch;
            
            while((ch = in.read()) != -1){
                sbf.append((char)ch);
            }
            
            if(sbf.length() > 0) message = sbf.toString();
            else throw new EmptyFileException(msgDest);
        }
        catch(IOException | EmptyFileException e){System.err.println(e.getMessage());}
    }
    
    Map<String,String> getProps(){
        return propsMap;
    }
    
    Map<String,String> getHosts(){
        return hostAuthentication;
    }
    
    List<String> getRecipients(){
        return recipients;
    }
    
    String getMessage(){
        return message;
    }
}
