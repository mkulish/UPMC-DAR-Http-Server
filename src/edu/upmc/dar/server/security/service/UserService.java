package edu.upmc.dar.server.security.service;


import edu.upmc.dar.server.security.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User get(Integer id);
    User saveOrUpdate(User point);
    void delete(User point);
    void delete(Integer id);
}
