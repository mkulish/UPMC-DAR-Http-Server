package edu.upmc.dar.server.security.entity;

public class User {
    private Integer id;
    private String login;
    private String password;

    public User() {
    }

    public User(User user) {
        this(user.getLogin(), user.getPassword());
        this.id = user.getId();
    }

    public User(String name, String password) {
        this.login = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User copy(){
        User result = new User(this.login, this.password);
        result.setId(this.id);
        return result;
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof User && ((User)other).login.equals(this.login) && ((User)other).password.equals(this.password));
    }

    @Override
    public int hashCode(){
        return (id != null) ? id : ((login == null) ? 1 : login.hashCode()) * ((password == null) ? 1 : password.hashCode());
    }
}
