package com.community.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/login1")
    public String test(){
        System.out.println("123");
        return "login";
    }
}
