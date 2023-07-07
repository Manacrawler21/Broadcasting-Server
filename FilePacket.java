package sample;

import java.io.Serializable;

public class FilePacket implements Serializable {
    private byte[] bytes;
    private String extension, type, message;
    public FilePacket(byte[] bytes, String extension, String author){
        this.bytes = bytes;
        this.extension = extension;
        this.type = "file";
        this.message = author + " sent a file.\n";
    }
    public FilePacket(String message)
    {
        this.message = message;
        this.type = "message";
    }
    byte[] getBytes()
    {
        return bytes;
    }
    String getExtension()
    {
        return extension;
    }
    String getType()
    {
        return type;
    }
    String getMessage()
    {
        return message;
    }

}
