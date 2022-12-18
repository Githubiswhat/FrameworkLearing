package cn.example.logFilter.starter.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
public class LogFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) {
        log.info("logFilter init...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //  从request中获取到访问的地址，并在控制台中打印出来
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("uri {} is working.", request.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("logFilter destroy...");
    }
}
