package net.catenax.ce.materialpass.http.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tools.httpTools;
import tools.logTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BaseInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) throws Exception {
            if(!httpRequest.getRequestURI().equals("/error")) {
                String httpInfo = httpTools.getHttpInfo(httpRequest, httpResponse.getStatus());
                logTools.printMessage(httpInfo);
            }
        return true;
    }
    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) throws Exception{
    }
}
