package sample;
import java.io.*;
import java.net.*;

public class WriteThread extends Thread{
    private ObjectOutputStream ous;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client)
    {
        this.socket = socket;
        this.client = client;
        try{
            OutputStream threadEndpoint = socket.getOutputStream();
            ous = new ObjectOutputStream(threadEndpoint);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("WriteThread Starting");
            Console console = System.console();
            String username = console.readLine("\nENTER USERNAME: ");
            client.setUsername(username);
            ous.writeObject(new FilePacket(username));
            String message;
            do {
                message = console.readLine("\n" + username + ": ");
                if (message.equals("sendfile"))
                {
                    String filename = "C:\\Users\\Austin\\Desktop\\Network Application\\src\\sample\\helloworld.txt";
                    FileInputStream fis = new FileInputStream(filename);
                    byte [] bytes = new byte[360000];
                    fis.read(bytes,0,(int) bytes.length);
                    String extension = filename.substring(filename.lastIndexOf("."),filename.length());
                    ous.writeObject(new FilePacket(bytes,extension,"\n" + username + " sent a file"));
                    fis.close();
                }
                else {
                    ous.writeObject(new FilePacket("\n" + username + ": " + message));
                }
            } while (!message.equals("bye"));
        }
        catch (IOException exception)
        {
            System.out.println("Error in WriteThread IO exception: " + exception.getMessage());
            exception.printStackTrace();
        }
        try{
            socket.close();
        }catch (IOException exception)
        {
            System.out.println("Error Occurred");
        }
    }
}
