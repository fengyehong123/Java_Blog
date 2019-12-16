package blog.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 自定义登录过滤拦截器
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        // 如果从session中获取不到用的话,就重定向到登录页面让用户登录
        if (request.getSession().getAttribute("user") == null){
            response.sendRedirect("/admin");
            return false;  // 不让请求继续执行
        }

        // 能获取到user数据,放行
        return true;
    }
}
