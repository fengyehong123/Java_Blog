package blog.util;

import blog.config.SmsProperties;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SendMessage {

    @Autowired
    private SmsProperties smsProperties;

    public String sendCode(String phone,String code,String signName, String template){

        // 从配置文件中获取秘钥
        String keyId = smsProperties.getAccessKeyId();
        String keySecret = smsProperties.getAccessKeySecret();

        DefaultProfile profile = DefaultProfile.getProfile("default", keyId, keySecret);

        // 初始化发送短信的客户端对象
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "default");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        try {
            CommonResponse commonResponse = client.getCommonResponse(request);
            String data = commonResponse.getData();
            return data;
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return null;

    }

}
