package com.community.test;

import com.community.common.Qiao;
import com.community.common.service.cache.RedisUtil;
import com.community.common.service.cache.MongodbUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class TestController {
    @Resource
    RedisUtil redisUtil;
    @RequestMapping("/login1")
    public String test(){
        Qiao qiao=new Qiao();
        qiao.setName("乔宏展");
        qiao.setAge(22);
        MongodbUtils.save(qiao);
        //redisUtil.set("ttt","sssss");
        System.out.println("123");
        return "login";
    }


}
