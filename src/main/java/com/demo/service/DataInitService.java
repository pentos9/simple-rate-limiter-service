package com.demo.service;

import com.demo.domain.User;
import com.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@Service
public class DataInitService {

    @Resource
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        log.info("Start to init data now...");
        for (int i = 0; i < 50; i++) {
            User user = new User();
            user.setPhoneNumber(RandomStringUtils.randomAlphanumeric(11));
            user.setAddress("address-" + UUID.randomUUID().toString());
            user.setUsername(RandomStringUtils.random(15, true, false));
            userRepository.save(user);
        }
    }
}
