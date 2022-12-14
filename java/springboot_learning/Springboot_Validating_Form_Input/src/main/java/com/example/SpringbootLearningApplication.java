package com.example;

import com.example.dao.UserRepository;
import com.example.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author windows
 */
@SpringBootApplication
@Slf4j
public class SpringbootLearningApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    public SpringbootLearningApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootLearningApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User("Sergey", 24, "1994-01-01");
        User user2 = new User("Ivan", 26, "1994-01-01");
        User user3 = new User("Adam", 31, "1994-01-01");
        log.info("Inserting data in DB.");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        log.info("User count in DB: {}", userRepository.count());
        log.info("User with ID 1: {}", userRepository.findById(1L));
        log.info("Deleting user with ID 2L form DB.");
        userRepository.deleteById(2L);
        log.info("User count in DB: {}", userRepository.count());
    }


}
