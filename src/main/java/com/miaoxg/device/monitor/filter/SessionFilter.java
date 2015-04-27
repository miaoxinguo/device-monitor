package com.miaoxg.device.monitor.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miaoxg.device.monitor.entity.User;

/**
 * Servlet Filter implementation class SessionFilter
 */
public class SessionFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(SessionFilter.class);
    
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest req = (HttpServletRequest)request;
	    HttpServletResponse resp = (HttpServletResponse)response;
	    
	    String requestURI = req.getRequestURI();
	    
        // 静态资源直接排除，即非/html目录下并且路径包含"."的请求
	    // TODO 这种方式不严谨，改为按目录判断，可以把所有静态资源统一放到/static目录下
        if(!requestURI.contains("/html") && requestURI.contains(".")){
            chain.doFilter(req, resp);
            return;
        }
        
	    // 登录页直接排除
	    if("/".equals(requestURI) || requestURI.endsWith("/login.html") || requestURI.endsWith("/auth")){
	        chain.doFilter(req, resp);
	        return;
	    }
	    
	    logger.debug(requestURI);
	    // 非登录页，不允许直接访问
	    User user = (User)req.getSession().getAttribute("user");
	    if(user==null){
	        // ajax请求设置为请求超时，非ajax请求为非法请求，直接跳转到登录页
	        String requestObj = req.getHeader("x-requested-with");
	        if(requestObj!=null && "XMLHttpRequest".equalsIgnoreCase(requestObj)){
	            resp.getWriter().print("timeout");
	        }else{
	           resp.sendRedirect("/device-monitor/login.html"); 
	        }
	        return;
	    }
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// ingore
	}
	
	/**
     * @see Filter#destroy()
     */
    public void destroy() {
        // ingore
    }
}
