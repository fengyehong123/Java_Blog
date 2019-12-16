package blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// 读取配置文件中我们自定义的发送短信的参数
@ConfigurationProperties(prefix = "aliyun.service")
public class SmsProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String verifyCodeTemplate;

    // 通过get和set方法读取配置文件中的属性值
    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getVerifyCodeTemplate() {
        return verifyCodeTemplate;
    }

    public void setVerifyCodeTemplate(String verifyCodeTemplate) {
        this.verifyCodeTemplate = verifyCodeTemplate;
    }
}
