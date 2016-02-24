package edu.upmc.dar.server.security.service.impl;

import edu.upmc.dar.server.security.entity.User;
import edu.upmc.dar.server.security.service.UserService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl instance;
    private UserServiceImpl(){}

    public static synchronized UserServiceImpl instance(){
        if(instance == null){
            instance = new UserServiceImpl();
        }
        return instance;
    }

    private List<User> list = new LinkedList<>();
    private int nextIdentity = 0;

    @Override
    public synchronized List<User> getAll() {
        List<User> listCopy = new ArrayList<>(list.size());
        list.forEach(point -> listCopy.add(point.copy()));
        return listCopy;
    }

    @Override
    public synchronized User get(Integer id) {
        if(id == null) return null;
        return list.stream().filter(point -> id.equals(point.getId())).findFirst().orElse(null);
    }

    @Override
    public synchronized User saveOrUpdate(User user) {
        if(user != null){
            User existing = get(user.getId());
            if(existing != null){
                //Updating the point
                existing.setName(user.getName());
                existing.setPassword(user.getPassword());
                user.setId(existing.getId());
                return existing;
            } else {
                //Saving the new point
                User newUser = user.copy();
                newUser.setId(nextIdentity++);
                user.setId(newUser.getId());
                list.add(newUser);
                return newUser;
            }
        }

        return null;
    }

    @Override
    public synchronized void delete(User user) {
        if(user != null){
            delete(user.getId());
        }
    }

    @Override
    public synchronized void delete(Integer id) {
        User existing = get(id);
        if(existing != null){
            list.remove(existing);
        }
    }
}
