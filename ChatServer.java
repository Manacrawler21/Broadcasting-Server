package sample;
import  java.net.*;
import  java.io.*;
import  java.util.*;
import  java.text.*;
import  java.time.*;
import  java.time.format.DateTimeFormatter;
public class ChatServer {
    private int port;
    private Set<String> names = new HashSet<>();
    private Set<Userthread> userthreads = new HashSet<>();

    public ChatServer(int port){
        this.port = port;
    }

    public void broadcast(FilePacket message, Userthread sender)
    {
        try(FileWriter fw = new FileWriter("chatlog.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            LocalDateTime timestamp = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String timestampstring = timestamp.format(formatter);
            out.println(timestampstring + "\n");
            out.println(message.getMessage());
            //more code
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        for(Userthread user: userthreads)
        {
            if(user != sender)
            {
                user.sendMessage(message);
            }
        }
    }

    //public void broadcastFile()

    public void addUsername(String name)
    {
        names.add(name);
    }
    public void addUserthread(Userthread userthread)
    {
        userthreads.add(userthread);
    }
    public boolean noUsers(){return userthreads.isEmpty();}
    public void removeUser(String name, Userthread userthread)
    {
        boolean isremoved = names.remove(name);
        if(isremoved)
        {
            userthreads.remove(userthread);
            System.out.println(name + "has left the chat");
        }
    }

    Set<String> getNames()
    {
        return names;
    }

    public void startserver()
    {


        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Listening on port " + port);
            while(true)
            {
                Socket serverEndpoint = serverSocket.accept();
                System.out.println("NEW USER CONNECTED. CURRENT NUMBER OF USERS: "+(userthreads.size()+1));
                Userthread client = new Userthread(serverEndpoint, this);
                userthreads.add(client);
                client.start();
            }
        }
        catch(IOException exception)
        {
            System.out.println("Error Encountered: "+ exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        int portnum = Integer.parseInt(args[0]);
        ChatServer server = new ChatServer(portnum);
        server.startserver();
    }
}