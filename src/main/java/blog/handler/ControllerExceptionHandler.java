package blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice  // 自定义的拦截器,会拦截被@Controller标记的所有的拦截器
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 标记方法是一个异常处理器的方法,可以拦截 Exception 级别的错误
    @ExceptionHandler(Exception.class)
    public ModelAndView ExceptionHandler(HttpServletRequest request, Exception e) throws Exception {

        // 把异常信息记录到日志中去
        logger.error("Request URL:{},Exception:{}",request.getRequestURI(),e);

        // 如果存在了指定状态的话,就抛出异常,交给SpringBoot处理
        // 如果是404资源找不到错误的话,就跳转到自定义的404页面去展示
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }

        // 除了404资源找不到的页面之外,所有的异常错误等都会跳转到error界面
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURI());
        mv.addObject("exception",e);
        mv.setViewName("error/error");

        return mv;
    }
}
