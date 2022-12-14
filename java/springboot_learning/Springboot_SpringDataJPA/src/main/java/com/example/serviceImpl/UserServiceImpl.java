package com.example.serviceImpl;


import com.example.bean.User;
import com.example.dao.UserDao;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userJpaDao;

    @Autowired
    public UserServiceImpl(UserDao userJpaDao) {
        this.userJpaDao = userJpaDao;
    }


    @Override
    public User getUserByName(String username) {
        return userJpaDao.findByName(username);
    }

    @Override
    public User findByAccount(String account) {
        return userJpaDao.findByAccount(account);
    }
}
