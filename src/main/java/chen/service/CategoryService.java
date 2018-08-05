package chen.service;

import chen.common.ServerResponse;
import chen.dto.CategoryDto;
import chen.entity.Category;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/1
 */
public interface CategoryService {

   /**
    * 获取所有分类
    * @return
    */
   List<Category> getCategories();

   /**
    * 根据分类ID删除分类
    * @param categoryId
    * @return
    */
   ServerResponse deleteCategoryById(Integer categoryId);

   /**
    * 通过ID查找分类
    * @param categoryId
    * @return
    */
   ServerResponse<CategoryDto> getCategoryById(Integer categoryId);

   /**
    * 更新分类
    * @param category
    * @return
    */
   ServerResponse updateCategory(CategoryDto category);

   ServerResponse saveCategory(CategoryDto category);
}
