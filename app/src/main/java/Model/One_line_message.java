package Model;

import java.io.Serializable;

public class One_line_message implements Serializable {
    private String Email;
    private String Name;
    private String Image;
    private String lastMessage;
    private String status;
    private String UID;
    public One_line_message(String email, String name, String image, String lastMessage, String status, String UID) {
        this.Email =    email;
        this.Name  =    name;
        this.Image =    image;
        this.lastMessage = lastMessage;
        this.status =      status;
        this.UID =         UID;
    }
    public One_line_message(){

    }
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
