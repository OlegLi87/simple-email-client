package emailclient;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<String,String> map = new HashMap<>();
    private static Protocol protocol = Protocol.IMAPS;
    
    // Main method gathers the required information about the connection and invokes start() method.
    public static void main(String[] args){
        putQuestions();
        map = UserScanner.getInput(map);
        
        String host = retrieveAnswer("host");
        String username = retrieveAnswer("username");
        String password = retrieveAnswer("password");
        
        // Starts listening process.
        MailListener.start(host, username, password, protocol); 
    }
    
    // Fills the map with the "hardcoded" questions that user will be asked about.
    private static void putQuestions(){
        map.put("Type in your host address...",null);
        map.put("Type in your username...",null);
        map.put("Type in your password...",null);
    }
    
    // Gets the answers from the map according to provided keyword.
    private static String retrieveAnswer(String keyword){
        String value = map.entrySet().stream()
                .filter(entry -> entry.getKey().contains(keyword))
                .map(entry -> entry.getValue())
                .findAny().get();
        return value;
    }
}
