package edu.upmc.dar.server.security.entity;

public class User {
    private Integer id;
    private String name;
    private String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User copy(){
        User result = new User(this.name, this.password);
        result.setId(this.id);
        return result;
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof User && ((User)other).name.equals(this.name) && ((User)other).password.equals(this.password));
    }

    @Override
    public int hashCode(){
        return (id != null) ? id : ((name == null) ? 1 : name.hashCode()) * ((password == null) ? 1 : password.hashCode());
    }
}
