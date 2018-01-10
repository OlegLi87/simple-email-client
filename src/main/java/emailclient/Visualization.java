package emailclient;

import java.util.concurrent.TimeUnit;

public class Visualization extends Thread {
    private static char c = '.';
    
    @Override
    public void run(){
        print();
    }
    // Method print that will be invoked in the call-stack of a new Thread.
    private void print(){   
            outerLoop:
            while(!this.isInterrupted()){
                int timeDuration = 0;
                System.out.print("Waiting for new messages from the server");
                while(timeDuration < 20){
                    timeDuration++;
                    System.out.print(c);
                    try{TimeUnit.SECONDS.sleep(1);}
                    catch(InterruptedException e){
                        System.out.println();
                        break outerLoop;
                    }
                }
                System.out.println();
                UserScanner.scanForExit();               
                if(this.isInterrupted()){
                   break outerLoop;
                }                                          
            }
        }
    // Method print that will be invoked in the call-stack of the calling Thread.
    public static void print(int timeDuration,String message){
        System.out.print(message);
        while(timeDuration > 0){
           timeDuration--;
           System.out.print(c);
           try{TimeUnit.SECONDS.sleep(1);}
           catch(InterruptedException e){e.printStackTrace();}
        }
        System.out.println();
    }
}
