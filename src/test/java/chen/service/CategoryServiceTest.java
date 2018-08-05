package chen.service;

import chen.entity.Category;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/3
 */
public class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testGetCategories(){
        List<Category> categories = categoryService.getCategories();
        for (Category category : categories) {
            System.out.println(category.getCategoryId());
            System.out.println(category.getCategoryName());
            System.out.println(category.getArticleNum());
        }
    }


}
