package blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect  // 标记是一个切面编程类
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 规定切面拦截的类
    @Pointcut("execution(* blog.web.*.*(..))")
    public void log(){

    }

    // 在切面之前执行
    @Before("log()")
    public void doBeforeAspect(JoinPoint joinPoint){

        // 上下文获取request对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 根据request对象获取url和ip
        String uri = request.getRequestURI();
        String addr = request.getRemoteAddr();

        // 获取类名方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        // 获取请求的参数
        Object[] args = joinPoint.getArgs();

        // 将参数放入我们自定义封装类中去
        RequestLog requestLog = new RequestLog(uri,addr,classMethod,args);

        // 记录切面中的方法执行之前的相关信息 (url,ip,方法名,参数)
        logger.info("Request : {}",requestLog);
    }

    // 在切面之后执行
    @After("log()")
    public void  doAfterAspect(){
        // logger.info("-----doAfter----------");
    }

    // 在切面执行之后执行,记录方法的返回
    @AfterReturning(returning = "result",pointcut = "log()")  // 捕获方法返回的内容
    public void doAfterReturn(Object result){

        logger.info("Result:{}",result);
    }

    // 封装的类
    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        // 请求的参数
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
