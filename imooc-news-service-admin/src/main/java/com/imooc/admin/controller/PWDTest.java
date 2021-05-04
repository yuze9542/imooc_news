package com.imooc.admin.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PWDTest {
    public static void main(String[] args) {
        String admin1 = BCrypt.hashpw("admin", BCrypt.gensalt());
        String admin2 = BCrypt.hashpw("admin2", BCrypt.gensalt());
        System.out.println(admin1);
        System.out.println(admin2);
    }
}
