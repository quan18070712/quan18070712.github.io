package Model;

public class User {
    private String Email;
    private String Name;
    private String Pass;
    private String status;
    private String uid;
    private String avatar;

    public User(String email, String name, String pass,String status, String uid,String avatar) {
        this.Email = email;
        this.Name = name;
        this.Pass = pass;
        this.status = status;
        this.uid = uid;
        this.avatar = avatar;

    }
    public User(){

    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }
}
