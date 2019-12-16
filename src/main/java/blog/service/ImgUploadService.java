package blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImgUploadService {

    String ImgUpload(MultipartFile multipartFile);
}
