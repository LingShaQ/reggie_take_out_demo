package com.lingsha.reggie.filter;

/**
 * @auther Ling.Sha
 * @date 2022/8/5 - 16:25
 */

import com.alibaba.fastjson.JSON;
import com.lingsha.reggie.common.BaseContext;
import com.lingsha.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 检查用户是否登陆
 */
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter  implements Filter {
    //路径匹配器,支持通配符写法
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();//进行路径比较的
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次uri
        String requestURI = request.getRequestURI();
        //不需要处理的路径
        String[] urls = new String[]
                {
                        "/employee/login",//登陆
                        "employee/logout",  // 登出
                        "/backend/**",//访问静态资源不用处理
                        "/front/**",
                        "/user/login",//用户登陆
                        "/user/sendMsg"//短信
                };

        //判断请求是否需要处理
        boolean check = check(urls, requestURI);
        if(check==true)
        {
            log.info("直接放行静态资源");
            filterChain.doFilter(request,response);//放行
            return;
        }
        //到这里说明不能直接放行，需要检查，登陆状态
        HttpSession session = request.getSession();
        if(session.getAttribute("employee")!=null)
        {
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);//设置id
            log.info("用户已经登陆，允许访问");
            filterChain.doFilter(request,response);//放行
            return;
        }
        //request.js文件
        //未登录，拦截
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("用户未登录，拦截到{}",requestURI);
        return;
    }

    /**
     * 路径匹配，是否放行
     * @param urls
     * @param requestUri
     * @return
     */
    public boolean check(String[] urls,String requestUri)
    {
    for (String url:urls)
    {
        boolean match = PATH_MATCHER.match(url, requestUri);
        if(match)
        {
            return true;
        }
    }
        return false;
    }
}
