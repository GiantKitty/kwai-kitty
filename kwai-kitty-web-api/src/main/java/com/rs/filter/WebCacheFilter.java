package com.rs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;

import lombok.extern.slf4j.Slf4j;

/**
 * 禁止浏览器缓存所有动态页面
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-20
 */
@WebFilter("/*")
@Slf4j
@Order(2)
public class WebCacheFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(filterConfig.getFilterName() + " is init !!!!");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        //让Web资源不缓存，很简单，设置http中response的请求头即可了！
        //我们使用的是http协议，ServletResponse并没有能够设置请求头的方法，所以要强转成HttpServletRequest
        //一般我们写Filter都会把他俩强转成Http类型的
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        response.setDateHeader("Expires", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");

        //放行目标资源的response已经设置成不缓存的了
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("WebCacheFilter is destroyed !!!!");
    }
}
