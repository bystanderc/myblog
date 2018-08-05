package chen.service.impl;

import chen.common.ServerResponse;
import chen.dao.CategoryDtoMapper;
import chen.dto.CategoryDto;
import chen.entity.Category;
import chen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/1
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDtoMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        return categoryMapper.getCategories();
    }

    @Override
    public ServerResponse deleteCategoryById(Integer categoryId) {
        int resultCount = categoryMapper.deleteByPrimaryKey(categoryId);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("删除分类失败");
        }
        return ServerResponse.createBySuccessMessage("删除分类成功");
    }

    @Override
    public ServerResponse<CategoryDto> getCategoryById(Integer categoryId) {
        CategoryDto categoryDto = categoryMapper.selectByPrimaryKey(categoryId);
        if (categoryDto == null){
            return ServerResponse.createByErrorMessage("未找到该分类");
        }
        return ServerResponse.createBySuccess(categoryDto);
    }

    @Override
    public ServerResponse updateCategory(CategoryDto category) {
        int resultCount = categoryMapper.updateByPrimaryKey(category);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("更新分类失败");
        }
        return ServerResponse.createBySuccessMessage("更新分类成功");
    }

    @Override
    public ServerResponse saveCategory(CategoryDto category){
        int resultCount = categoryMapper.insert(category);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("添加分类失败");
        }
        return ServerResponse.createBySuccessMessage("添加分类成功");
    }
}
