package sample;
import java.io.*;
import java.net.*;

public class ReadThread implements Runnable{
    private ObjectInputStream reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client){
        this.socket = socket;
        this.client = client;
        try{
            System.out.println("ReadThread Starting");
            InputStream inputStream = socket.getInputStream();
            reader =new ObjectInputStream(inputStream);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                //System.out.println("ReadThread Starting");
                FilePacket received = (FilePacket) reader.readObject();
                String messages = received.getMessage();
                System.out.println(messages);
                if(received.getType().equals("file")){
                    System.out.println("File Received");
                    FileOutputStream fout = new FileOutputStream("C:\\Users\\Austin\\Desktop\\Network Application\\src\\TEST\\"+client.getUsername()+received.getExtension());
                    fout.write(received.getBytes(),0,received.getBytes().length);
                    fout.close();
                }
                if (client.getUsername() != null)
                {
                    System.out.println(client.getUsername()+": ");
                }
            }
            catch (IOException exception)
            {
                System.out.println("Error reading from server: " + exception.getMessage());
                exception.printStackTrace();
                break;
            }
            catch (ClassNotFoundException exception)
            {
                System.out.println("Error in ReadThread ClassNotFound Exception: "+exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
}
