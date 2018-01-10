package emailclient;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class UserScanner{
    
    private static Thread main = Thread.currentThread();
    private static Scanner scanner;
    
    // Retrieves entries from the map and forms a new map
    //  where a key is question and a value is user's answer.
    public static Map<String,String> getInput(Map<String,String> map){
        map = map.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey(), UserScanner::scanInput));
        return map;
    }
    
    // Scans the input from user and returning the value as Map value.
    private static String scanInput(Map.Entry<String,String> entry){
        System.out.println(entry.getKey());
      
        scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }
    
    // Scans user's input for "yes".
    public static String scanForYes(int length){
        System.out.println(String.format("You have %1d new message/s",length));
        System.out.println("To see them type in [yes]?");     
       
        scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }
    
    // Scans user's input for "exit".
    public static void scanForExit(){
        System.out.println("To exit type [exit]");
     
        scanner = new Scanner(System.in);
        String input = scanner.nextLine();    // nexline() reads also empty String whereas next() method doesnt.
       
        if(input.equalsIgnoreCase("exit")){
            main.interrupt();                   // If user types "exit" sends the interrupt command to main Thread.
            Thread.currentThread().interrupt();           
        }
    }
    // Scans for user's input "save".
    public static boolean scanForSave(){
      System.out.println("To save that message on you local machine type [save]?");
     
      scanner = new Scanner(System.in);    
      String input = scanner.nextLine();
      
      if(input.equalsIgnoreCase("save")) return true;
      else return false;  
    }
}
