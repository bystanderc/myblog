package chen.entity;

/**
 * @author bystander
 * @date 2018/8/2
 */
public class Category {
    private  int categoryId;
    private String categoryName;
    private int articleNum;

    public int getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public Category() {
    }

    public Category(int categoryId, String categoryName, int articleNum) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.articleNum = articleNum;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
