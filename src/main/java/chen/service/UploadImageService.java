package chen.service;

import chen.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author bystander
 * @date 2018/8/4
 */
public interface UploadImageService {

    ServerResponse uploadImage(MultipartFile items_pic, String imagePath);
}
