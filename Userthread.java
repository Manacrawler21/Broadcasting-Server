
package sample;
import java.util.*;
import java.net.*;
import java.io.*;

public class Userthread extends Thread {
    private Socket socket;
    private ChatServer server;
    private ObjectOutputStream ous;

    Userthread(Socket socket, ChatServer server)
    {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ous = new ObjectOutputStream(outputStream);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //writer = new PrintWriter(outputStream, true);

            printAllUsers();
            FilePacket packet = (FilePacket) objectInputStream.readObject();

            String username = packet.getMessage();
            server.addUsername(username);
            server.broadcast(new FilePacket("NEW USER CONNECTED! WELCOME "+ username) , this);
            String message;
            do {
                FilePacket received = (FilePacket) objectInputStream.readObject();
                message = received.getMessage();
                //servermessage = username + ": " + message + "\n";
                server.broadcast(received, this);

            } while (!message.equals("bye"));
            server.removeUser(username, this);
            socket.close();
            FilePacket exitMessage = new FilePacket("The user " + username + " has left the server");
            server.broadcast(exitMessage, this);
        }
        catch (IOException exception)
        {
            System.out.println("Error in UserThread IOexception: " + exception.getMessage());
            exception.printStackTrace();
        }
        catch (ClassNotFoundException exception)
        {
            System.out.println("Error in UserThread ClassNotFound Exception: "+exception.getMessage());
            exception.printStackTrace();
        }
    }
    void sendMessage(FilePacket message){
        try{
        ous.writeObject(message);
        }
        catch (IOException exception){
            System.out.println("Error in UserThread sendMessage: " + exception.getMessage());
            exception.printStackTrace();
        }

    }

    void printAllUsers()
    {
        try {
            if (server.noUsers()) {
                ous.writeObject(new FilePacket("\nNO USERS CONNECTED"));
            } else {
                ous.writeObject(new FilePacket("\nUSERS CONNCETED: " + server.getNames()));
            }
        }
        catch (IOException exception){
            System.out.println("Error in UserThread sendMessage: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

}


