package blog.web.admin;

import blog.pojo.User;
import blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class  LoginController {

    @Autowired
    private UserService userService;

    // 访问连接,跳转到登录界面
    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")  // POST方式提交数据,用 @RequestParam 来接收参数
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes attributes
    ){

        User user = userService.checkUser(username, password);
        if (user != null){
            // 不能把密码传输前端
            user.setPassword(null);
            // 如果能查询到用户.就把用户信息放到session中
            session.setAttribute("user",user );
            return "admin/index";
        } else {
            // 用户名或者密码不正确的话,通过重定向属性把信息返回给前端的提示界面中
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin";
        }
    }

    // 用户注销
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
