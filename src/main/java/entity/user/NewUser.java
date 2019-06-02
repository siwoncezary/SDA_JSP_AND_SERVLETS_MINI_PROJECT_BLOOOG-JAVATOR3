package entity.user;

public class NewUser {
    public final String email;
    public final String nick;
    public final String password;

    public NewUser(String email, String nick, String password) {
        this.email = email;
        this.nick = nick;
        this.password = password;

    }

    public NewUser(UserEntity ue){
        this.email = ue.getEmail();
        this.nick = ue.getNick();
        this.password = ue.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "NewUser{" +
                "email='" + email + '\'' +
                ", nick='" + nick + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
