package com.rs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.rs.dao.UserDao;
import com.rs.model.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-20
 */
@WebFilter("/rest/o/w/creator/login")
@Order(3)
@Slf4j
public class LoginFilter implements Filter {

    @Autowired
    private UserDao userDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(filterConfig.getFilterName() + " is init !!!!");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        log.info("LoginFilter before !!!!");
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;

        //如果用户没有关闭浏览器，就不需要Cookie做拼接登陆了
        if (request.getSession().getAttribute("user") != null) {
            log.info("user has login");
            filterChain.doFilter(request, response);
            return;
        }

        //用户关闭了浏览器，session的值就获取不到了。所以要通过Cookie来自动登陆
        Cookie[] cookies = request.getCookies();
        String value = null;
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            if (cookies[i].getName().equals("autoLogin")) {
                value = cookies[i].getValue();
            }
        }

        //得到Cookie的用户名和密码
        if (value != null) {
            String username = value.split("\\.")[0];
            String password = value.split("\\.")[1];

            User user = userDao.find(username, password);

            if (user != null) {
                request.getSession().setAttribute("user", user);
            }
        }

        filterChain.doFilter(request, response);
        log.info("LoginFilter after !!!!");
    }

    @Override
    public void destroy() {
        log.info("LoginFilter is destroyed !!!!");
    }
}
