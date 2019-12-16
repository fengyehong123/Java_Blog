package blog.service.Impl;

import blog.config.ImgUploadConfig;
import blog.service.ImgUploadService;
import com.aliyun.oss.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import java.io.InputStream;

@Service
@EnableConfigurationProperties(ImgUploadConfig.class)
public class ImgUploadServiceImpl implements ImgUploadService {

    @Autowired
    private ImgUploadConfig config;

    @Override
    public String ImgUpload(MultipartFile multipartFile) {

        // 获取阿里云的key和id
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessKeySecret();
        // 储存空间区域
        String endpoint = "http://oss-cn-qingdao.aliyuncs.com";
        // 照片的原始名称
        String originalFilename = multipartFile.getOriginalFilename();
        // 完整名称,保证每个照片名称不同 通过时间戳的方式
        String imgName = System.currentTimeMillis() + originalFilename;
        // 储存空间名称
        String bucketName = "markdowmwimg";

        try {
            // 获取文件上传的输入流
            InputStream inputStream = multipartFile.getInputStream();
            // 校验上传的数据是不是图片,如果是图片的话,就上传
            //BufferedImage imgData = ImageIO.read(inputStream);
            // 创建oss客户端
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            try {
                // 储存空间名称  照片名称  前端传出来的照片输入流
                ossClient.putObject(bucketName, imgName, inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 关闭客户端
            ossClient.shutdown();

            return "https://" + bucketName + ".oss-cn-qingdao.aliyuncs.com/" + imgName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
