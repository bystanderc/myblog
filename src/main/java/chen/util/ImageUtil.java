package chen.util;

import java.io.File;

/**
 * @author bystander
 * @date 2018/8/3
 */
public class ImageUtil {
    //获取图片
    public static String[] getImages(String path){
        File imgFile = new File(path);
        String[] images = imgFile.list();
        return images;
    }
}
