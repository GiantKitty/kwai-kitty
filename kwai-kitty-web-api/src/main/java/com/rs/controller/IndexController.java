package com.rs.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class IndexController {

    @RequestMapping("/hello")
    public String hello() {
        return "嗨，你好😊。";
    }

    /**
     * 测试慢执行
     */
    @RequestMapping("/hello/cycle")
    public String helloCycle(@RequestParam Integer cycle) throws InterruptedException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < cycle; i++) {
            TimeUnit.MILLISECONDS.sleep(10);
            System.out.println(i + " 嗨，你好 " + LocalDateTime.now().format(dtf));
        }
        return "hello cycle over";
    }

    /**
     * 测试异常
     */
    @RequestMapping("/division")
    public String division() {
        System.out.println(1 / 0);
        return "division over";
    }


}
