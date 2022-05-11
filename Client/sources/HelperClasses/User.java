package HelperClasses;

public class User {
    public int id;
    public String e_mail;
    public String passwd;
    public int role;

    public User(int id, String e_mail, String passwd, int role) {
        this.id = id;
        this.e_mail = e_mail;
        this.passwd = passwd;
        this.role = role;
    }

    public int getId() {
        return this.id;
    }

    public int getRole() {
        return this.role;
    }

    public String get_e_mail() {
        return this.e_mail;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
