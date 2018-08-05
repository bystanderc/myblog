package chen.service;

import chen.common.ServerResponse;
import chen.dto.ArticleDto;
import chen.entity.Article;
import chen.entity.ArticleLite;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/1
 */
public interface ArticleService {

    /**
     * 获取所有article
     * @return
     */
    List<ArticleDto> getArticles();

    /**
     * 通过分类id获取给分类的中的所有文章
     * @param id
     * @return
     */
    List<ArticleLite> getArticlesByCategoryId(Integer id);

    /**
     * 通过文章ID获取文章
     * @param id
     * @return
     */
    ServerResponse<ArticleDto> getArticleById(Integer id);

    /**
     * 获取前一篇文章
     * @param articleId
     * @return
     */
    ArticleLite getPreArticle(Integer articleId);

    /**
     * 获取下一篇文章
     * @param articleId
     * @return
     */
    ArticleLite getNextArticle(Integer articleId);

    /**
     * 增加文章点击次数
     * @param articleId
     * @return
     */
    ServerResponse addClick(Integer articleId);

    /**
     * 删除文章
     * @param articleId
     * @return
     */
    ServerResponse deleteArticle(Integer articleId);

    /**
     * 保存文章
     * @param article
     * @return
     */
    ServerResponse saveArticle(Article article);

    /**
     * 最近的文章
     * @return
     */
    List<ArticleLite> getRecentArticles();

    /**
     * 获取最常浏览的文章
     * @return
     */
    List<ArticleLite> getMostViewedArticles();

    /**
     * 根据关键字搜索文章
     * @param key
     * @return
     */
    List<ArticleDto> searchArticles(String key);

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getArticlesByPaging(Integer pageNum, Integer pageSize);

    /**
     * 更新文章
     * @param article
     * @return
     */
    ServerResponse updateArticle(Article article);
}
