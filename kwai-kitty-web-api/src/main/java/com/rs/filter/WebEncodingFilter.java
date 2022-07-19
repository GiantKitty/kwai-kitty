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
 * 解决中文乱码问题
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-20
 */
//过滤全部内容
@WebFilter("/*")
@Slf4j
@Order(1)//指定过滤器的执行顺序,值越大越靠后执行
//@Component//无需添加此注解，在启动类添加@ServletComponentScan注解后，会自动将带有@WebFilter的注解进行注入！
public class WebEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(filterConfig.getFilterName() + " is init !!!!");
//        System.out.println("getFilterName:"+filterConfig.getFilterName()); //返回<filter-name>元素的设置值。
//        System.out.println("getServletContext:"+filterConfig.getServletContext()); //返回FilterConfig对象中所包装的ServletContext对象的引用。
//        System.out.println("getInitParameter:"+filterConfig.getInitParameter("cacheTimeout")); //用于返回在web.xml文件中为Filter所设置的某个名称的初始化的参数值
//        System.out.println("getInitParameterNames:"+filterConfig.getInitParameterNames()); //返回一个Enumeration集合对象。
    }

    /**
     * 入参是ServletRequest，而不是httpservletrequest。因为过滤器是在httpservlet之前。
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) req;
        HttpServletResponse response=(HttpServletResponse) resp;
        request.setCharacterEncoding("utf-8");
        response.setContentType("html/text;charset=utf-8");
        log.info("WebEncodingFilter is working !!!!");
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        log.info("WebEncodingFilter is destroyed !!!!");
    }
}
