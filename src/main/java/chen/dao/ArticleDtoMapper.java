package chen.dao;

import chen.dto.AboutDto;
import chen.dto.ArticleDto;
import chen.entity.Article;
import chen.entity.ArticleLite;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ArticleDtoMapper {

    int deleteByPrimaryKey(Integer articleid);

    int insert(Article record);

    ArticleDto selectByPrimaryKey(Integer articleid);

    int updateByPrimaryKey(Article record);

    AboutDto getAbout();

    int updateAbout(String content);

    List<ArticleDto> getArticles();

    List<ArticleLite> getArticlesByCategoryId(Integer id);

    ArticleLite getPreArticle(Integer id);

    ArticleLite getNextArticle(Integer id);

    int addClick(Integer articleId);

    List<ArticleLite> getRecentArticles();

    List<ArticleLite> getMostViewedArticles();

    List<ArticleDto> getArticlesByKey(String key);



}