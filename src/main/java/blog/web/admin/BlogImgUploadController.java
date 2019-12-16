package blog.web.admin;

import blog.service.ImgUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class BlogImgUploadController {

    @Autowired
    private ImgUploadService imgUploadService;


    @PostMapping("/blogImg")
    public ResponseEntity<Map> ImgUpload(MultipartFile file){

        String imgAddr = imgUploadService.ImgUpload(file);

        // 如果返回的地址为空
        if (urlIsBlank(imgAddr)){
            return ResponseEntity.badRequest().build();
        }

        // 如果地址不为空,就封装传回前端json对象
        Map<String, String> map = new HashMap<>();
        map.put("code","200");
        map.put("ImgAddr",imgAddr);

        // 返回json对象到前端
        return ResponseEntity.status(HttpStatus.CREATED).body(map);

    }

    // 校验返回的地址是否为空
    public static boolean urlIsBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
