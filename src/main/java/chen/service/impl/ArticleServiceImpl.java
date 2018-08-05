package chen.service.impl;

import chen.common.ServerResponse;
import chen.dao.ArticleDtoMapper;
import chen.dto.ArticleDto;
import chen.entity.Article;
import chen.entity.ArticleLite;
import chen.service.ArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/1
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDtoMapper articleMapper;

    @Override
    public List<ArticleDto> getArticles() {
        return articleMapper.getArticles();
    }

    @Override
    public List<ArticleLite> getArticlesByCategoryId(Integer id){
        return articleMapper.getArticlesByCategoryId(id);
    }

    @Override
    public ServerResponse<ArticleDto> getArticleById(Integer id) {

        ArticleDto articleDto = articleMapper.selectByPrimaryKey(id);

        if (articleDto == null){
            return ServerResponse.createByErrorMessage("该文章不存在");
        }
        return ServerResponse.createBySuccess(articleDto);
    }

    @Override
    public ArticleLite getPreArticle(Integer articleId) {
        ArticleLite preArticle = articleMapper.getPreArticle(articleId);
        if (preArticle == null){
            return new ArticleLite(-1, "这已经是第一篇了");
        }
        return preArticle;
    }

    @Override
    public ArticleLite getNextArticle(Integer articleId) {
        ArticleLite nextArticle = articleMapper.getNextArticle(articleId);
        if (nextArticle == null){
            new ArticleLite(-1, "这已经是最后一篇了");
        }
        return nextArticle;
    }

    @Override
    public ServerResponse addClick(Integer articleId) {
        int resultCount = articleMapper.addClick(articleId);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("点击量增加失败");
        }
        return ServerResponse.createBySuccessMessage("点击量增加成功");
    }

    @Override
    public ServerResponse deleteArticle(Integer articleId) {
        int resultCount = articleMapper.deleteByPrimaryKey(articleId);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("删除文章失败");
        }
        return ServerResponse.createBySuccessMessage("删除文章成功");
    }

    @Override
    public ServerResponse saveArticle(Article article) {
        int resultCount = articleMapper.insert(article);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("添加文章失败");
        }
        return ServerResponse.createBySuccessMessage("添加文章成功");
    }

    @Override
    public List<ArticleLite> getRecentArticles() {
        return articleMapper.getRecentArticles();
    }

    @Override
    public List<ArticleLite> getMostViewedArticles() {
        return articleMapper.getMostViewedArticles();
    }

    @Override
    public List<ArticleDto> searchArticles(String key) {
        return articleMapper.getArticlesByKey(key);
    }

    @Override
    public ServerResponse<PageInfo> getArticlesByPaging(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ArticleDto> articleDtos = articleMapper.getArticles();
        PageInfo pageResult = new PageInfo(articleDtos);
        pageResult.setList(articleDtos);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse updateArticle(Article article){
        int resultCount = articleMapper.updateByPrimaryKey(article);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("更新文章失败");
        }
        return ServerResponse.createBySuccessMessage("更新成功");
    }

}
