package com.rs.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rs.dao.UserDao;
import com.rs.model.User;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-20
 */
@Lazy
@RestController
public class LoginController {

    @Autowired
    private UserDao userDao;

    /**
     * 浏览器第一次访问服务器时，服务器会校验用户名和密码是否存在，
     * 如果存在则生成一个小文件cookie，cookie上记录着用户名和密码，把小文件发送给浏览器保存（需要设置有效时间）
     */
    @ResponseBody
    @RequestMapping("/rest/o/w/creator/setCookie")
    public String myCookie(HttpServletResponse response) {

        Cookie cookie = new Cookie("user", "kitty");
        //指定cookie绑定路径  setPath  在非springboot项目里需要添加项目的名称（request.getcontestpath（）+路径）
        //一个cookie如果没有设置有效的路径，那么默认是当前工程，如果设置了，当只有访问当前路径，浏览器才会将cookie发送给服务器
        cookie.setPath("/cookie");
        //给cookie设置有效期  单位秒   setMaxAge
        cookie.setMaxAge(60 * 60 * 24 * 10);
        //把生成的cookie写到响应的客户端
        response.addCookie(cookie);
        return "my cookie";
    }

    /**
     * 从请求中获取cookie的name与value
     */
    @ResponseBody
    @RequestMapping("/rest/o/w/creator/getCookie")
    public String findAllCookie(HttpServletRequest request) {
        //从请求中获取cookie的name与value
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName() + "====" + cookie.getValue());
            if (cookie.getName().equals("student") && cookie.getValue().equals("songXianYang")) {
                //执行相应的代码
            } else {
                //否则执行什么
            }
        }
        return "从请求中获取cookie的name与value";
    }

    @RequestMapping("/rest/o/w/creator/login")
    public Map<String, Object> home(@RequestParam String username, @RequestParam String password,
            @RequestParam int time, HttpServletResponse response, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = userDao.find(username, password);
        if (user == null) {
            result.put("message", "用户名或密码是错的！");
            return result;
        }
        //如果不是为空，那么在session中保存一个属性
        session.setAttribute("user", user);
        result.put("message", "恭喜你，已经登陆了！");

        //如果想要用户关闭了浏览器，还能登陆，就必须要用到Cookie技术了
        Cookie cookie = new Cookie("autoLogin", user.getUsername() + "." + user.getPassword());
        //设置Cookie的最大声明周期为用户指定的
        cookie.setMaxAge(Integer.parseInt(String.valueOf(time * 60)));  // 1min
        //把Cookie返回给浏览器
        response.addCookie(cookie);

        return result;
    }

}
