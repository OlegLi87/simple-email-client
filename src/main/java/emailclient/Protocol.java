package emailclient;

public enum Protocol {
    IMAPS ("imaps"),
    POP3 ("pop3s");
    
    private String protocol;
    
    private Protocol(String protocol){
        this.protocol = protocol;
    }
    String getProtocol(){
        return protocol;
    }
}

