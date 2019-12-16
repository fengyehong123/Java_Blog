package blog.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.ArrayList;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 我们自定义的拦截器
        LoginInterceptor loginInterceptor = new LoginInterceptor();

        // 指定要排除不过滤的url路径
        ArrayList<String> list = new ArrayList<>();
        list.add("/admin");
        list.add("/admin/login");

        // 把我们自定义的拦截器放入拦截配置文件中
        registry.addInterceptor(loginInterceptor)
                // 拦截admin下面的所有的请求
                .addPathPatterns("/admin/**")
                // 指定某个路径不被拦截
                .excludePathPatterns(list);
    }
}
