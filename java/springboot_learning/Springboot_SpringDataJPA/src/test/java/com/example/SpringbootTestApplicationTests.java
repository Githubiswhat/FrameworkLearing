package com.example;

import com.example.bean.User;
import com.example.dao.UserDao;
import com.example.util.RandomStringGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

@SpringBootTest
class SpringbootTestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private UserDao userDao;

    int nums = 10;
    String[] names = new String[nums];
    String[] accounts = new String[nums];
    String[] pwd = new String[nums];

    @Test
    public void testAdd() {
        int length = 10;
        for (int i = 0; i < nums; i++) {
            names[i] = RandomStringGenerator.getRandomAlphabetic(length);
            accounts[i] = RandomStringGenerator.getRandomAlphabetic(length);
            pwd[i] = RandomStringGenerator.getRandomAlphanumeric(length);
        }

        for (int i = 0; i < nums; i++) {
            User user = new User((long) i, names[i], accounts[i], pwd[i]);
            userDao.save(user);
        }

    }


    @Test
    public void testLocate() {
        for (int i = 0; i < nums; i++) {
            Optional<User> userOptional = userDao.findById((long) i);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                System.out.println("name = " + user.getName());
                System.out.println("account = " + user.getAccount());
            }
        }
    }

    @Test
    public void testDel() {
        for (int i = 0; i < nums; i++) {
           userDao.deleteById((long) i);
        }
    }
}
