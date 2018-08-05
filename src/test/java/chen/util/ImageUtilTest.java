package chen.util;

import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bystander
 * @date 2018/8/4
 */
public class ImageUtilTest extends BaseTest {



    @Test
    public void testImage(){
        String[] articleImages = ImageUtil.getImages("/Users/bystander/IdeaProjects/myblog/src/main/webapp/images/article");
        String[] userImages = ImageUtil.getImages("/Users/bystander/IdeaProjects/myblog/src/main/webapp/images/user");
        for (String articleImage : articleImages) {
            System.out.println("articleImage = " + articleImage);
        }

    }

}
