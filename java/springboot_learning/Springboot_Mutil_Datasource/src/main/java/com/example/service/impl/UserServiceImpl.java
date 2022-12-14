package com.example.service.impl;

import com.example.dao.cluster.CityDao;
import com.example.dao.master.UserDao;
import com.example.domain.City;
import com.example.domain.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户业务实现层
 * <p>
 * Created by bysocket on 07/02/2017.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao; // 主数据源

    private final CityDao cityDao; // 从数据源

    public UserServiceImpl(UserDao userDao, CityDao cityDao) {
        this.userDao = userDao;
        this.cityDao = cityDao;
    }

    @Override
    public User findByName(String userName) {
        User user = userDao.findByName(userName);
        City city = cityDao.findByName("温岭市");
        user.setCity(city);
        return user;
    }
}
