package sample;
import java.net.*;
import java.io.*;
public class ChatClient {
    private String username;
    private int port;
    private String hostname;
    public ChatClient(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    public void start(){
        try{
            Socket socket = new Socket(hostname, port);
            System.out.println("CONNECTED TO SERVER AT " + port);
            new WriteThread(socket,this).start();
            new ReadThread(socket,this).run();


        }
        catch (UnknownHostException exception)
        {
            System.out.println("ServerNotFound "+exception.getMessage());
        }
        catch (IOException exception)
        {
            System.out.println("IO ERROR "+exception.getMessage());
        }
    }

    void setUsername(String username)
    {
        this.username = username;
    }

    String getUsername()
    {
        return this.username;
    }
    public static void main(String[] args){
        int port = Integer.parseInt(args[1]);
        ChatClient client = new ChatClient(args[0],port);
        client.start();
    }

}
