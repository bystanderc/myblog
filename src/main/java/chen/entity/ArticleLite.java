package chen.entity;

import java.util.Date;

/**
 * @author bystander
 * @date 2018/8/1
 */
public class ArticleLite {
    private int articleId;
    private String title;
    private Date pubDate;

    public ArticleLite() {
    }

    public ArticleLite(int articleId, String title) {
        this.articleId = articleId;
        this.title = title;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
}
