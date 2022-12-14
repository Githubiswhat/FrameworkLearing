package com.example.service;

import com.example.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户业务接口层
 * <p>
 * Created by bysocket on 07/02/2017.
 */
@Mapper
public interface UserService {

    /**
     * 根据用户名获取用户信息，包括从库的地址信息
     *
     * @param userName
     * @return
     */
    User findByName(String userName);
}
