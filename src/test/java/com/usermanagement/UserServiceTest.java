package com.usermanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.usermanagement.dto.UserDTO;
import com.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
public class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void testAddUser() {
        UserDTO admin = new UserDTO();
        admin.setUserId("admin");
        admin.setUserPw("1234");
        admin.setUserNm("테스트어드민");
        admin.setUserAuth("SYSTEM_ADMIN");
        userService.addUser(admin);

        UserDTO user = new UserDTO();
        user.setUserId("user");
        user.setUserPw("1234");
        user.setUserNm("테스트유저");
        user.setUserAuth("USER");
        userService.addUser(user);

        log.info("1. testAddUser addAdmin : {}, addUser : {}", admin, user);
    }
}
