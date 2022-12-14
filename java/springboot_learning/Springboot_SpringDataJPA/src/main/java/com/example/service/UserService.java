package com.example.service;


import com.example.bean.User;

/**
 * The Interface UserService.
 */
public interface UserService {

    /**
     * Gets the user by name.
     *
     * @param username the user name
     * @return the user by name
     */
    User getUserByName(String username);

    User findByAccount(String account);


}
