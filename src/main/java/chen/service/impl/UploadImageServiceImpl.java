package chen.service.impl;

import chen.common.ServerResponse;
import chen.service.UploadImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author bystander
 * @date 2018/8/4
 */
@Service("uploadImageService")
public class UploadImageServiceImpl implements UploadImageService {

    private static final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);

    @Override
    public ServerResponse uploadImage(MultipartFile items_pic, String imagePath) {
        //原始名称
        String originalFilename = items_pic.getOriginalFilename();
        //上传图片
        if (items_pic != null && originalFilename != null && originalFilename.length() > 0) {
            //新的图片名称
            String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            //新图片
            File newFile = new File(imagePath + newFileName);
            //将内存中的数据写入磁盘
            try {
                items_pic.transferTo(newFile);
                return ServerResponse.createBySuccessMessage("上传成功");
            } catch (IOException e) {
                logger.error("上传图片异常");
                return ServerResponse.createByErrorMessage("上传失败");
            }
        }
        return ServerResponse.createByErrorMessage("参数错误");
    }
}
