package chen.service;

import chen.common.ServerResponse;
import chen.dto.ArticleDto;
import chen.entity.ArticleLite;
import com.github.pagehelper.PageInfo;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/3
 */
public class ArticleServiceTest extends BaseTest {

    @Autowired
    private ArticleService articleService;

    @Test
    public void testGetAllArticles(){
        List<ArticleDto> articles = articleService.getArticles();
        for (ArticleDto article : articles) {
            System.out.println(article.getTitle());
        }
    }

    @Test
    public void testGetArticlesByCategoryId(){
        List<ArticleLite> articles = articleService.getArticlesByCategoryId(3);
        for (ArticleLite article : articles) {

            System.out.println(article.getTitle());
        }
    }

    @Test
    public void testGetPreArticle(){
        ArticleLite preArticle = articleService.getPreArticle(3);
        System.out.println(preArticle.getTitle());
    }

    @Test
    public void testNextPreArticle(){
        ArticleLite nextArticle = articleService.getNextArticle(3);
        System.out.println(nextArticle.getTitle());
    }

    @Test
    public void testGetRecentArticles(){
        List<ArticleLite> recentArticles = articleService.getRecentArticles();
        for (ArticleLite recentArticle : recentArticles) {

            System.out.println(recentArticle.getTitle());
        }
    }

    @Test
    public void testGetArticlesByPaging(){
        ServerResponse<PageInfo> page = articleService.getArticlesByPaging(1, 2);
        PageInfo<ArticleDto> data = page.getData();
        List<ArticleDto> list = data.getList();
        System.out.println(data.getTotal());

        for (ArticleDto articleDto : list) {
            System.out.println(articleDto);
        }
    }

    @Test
    public void testGetArticleById(){
        ServerResponse<ArticleDto> res = articleService.getArticleById(2);
        ArticleDto data = res.getData();
        System.out.println(data.getCategory().getCategoryName());

    }

}
