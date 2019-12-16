package blog.web;

import blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    // 跳转登录页
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/checkName")
    public ResponseEntity<Boolean> checkUserName(String username){

        String userName = userService.checkUserName(username);

        if (!"".equals(userName)&& userName != null){
            return ResponseEntity.ok(false);
        } else {
            return ResponseEntity.ok(true);

        }
    }

    @GetMapping("/checkMobile")
    public ResponseEntity<Boolean> checkMobile(String mobile){
        String result = userService.checkMobile(mobile);
        if (!"".equals(result)&& result != null){
            return ResponseEntity.ok(false);
        } else {
            return ResponseEntity.ok(true);

        }
    }

    @PostMapping("/sendCode")
    public ResponseEntity<Boolean> sendCode(@RequestParam("mobileNum") String mobileNum) {

        System.out.println(mobileNum);
        String result = userService.sendCode(mobileNum);

        return null;

    }
}
