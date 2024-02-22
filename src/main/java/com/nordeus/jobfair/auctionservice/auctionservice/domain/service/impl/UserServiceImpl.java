package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.impl;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    public static List<User> userList = new ArrayList<>();


    @Override
    public User register(String username, String password) {

        String id = username+String.valueOf(System.currentTimeMillis());
        User u = new User(new UserId(id),username,password);
        for(User userInList : userList){
            if(userInList.getUsername().equals(username)){
                throw new RuntimeException("User Already Exists");
            }
        }
        userList.add(u);
        return u;
    }

    @Override
    public User login(String username, String password) {

        for(User userInList : userList){
            if(userInList.getUsername().equals(username) && userInList.getPassword().equals(password)){
                return userInList;
            }
        }
        throw new RuntimeException("Wrong credentials");
    }
}
