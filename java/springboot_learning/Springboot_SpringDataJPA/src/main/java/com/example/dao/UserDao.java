package com.example.dao;


import com.example.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

    User findByName(String name);

    User findByAccount(String account);

}