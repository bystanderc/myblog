package chen.web;

import chen.common.ServerResponse;
import chen.dto.AboutDto;
import chen.dto.ArticleDto;
import chen.entity.ArticleLite;
import chen.entity.Category;
import chen.entity.Link;
import chen.service.AboutService;
import chen.service.ArticleService;
import chen.service.CategoryService;
import chen.service.LinkService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author bystander
 * @date 2018/8/1
 */
@Controller
public class BlogController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AboutService aboutService;

    @Autowired
    private LinkService linkService;

    /**
     * 生成翻页按钮
     *
     * @param totalNum 查询的总记录
     * @param pageNum  当前页
     * @param pageSize 每页显示数
     * @param search   搜索关键词
     * @return
     */
    private static String pagingButton(int totalNum, int pageNum, int pageSize, String search) {
        int totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        StringBuffer pageCode = new StringBuffer();
        pageCode.append("<ul class=\"pagination\">");
        pageCode.append("<li class='waves-effect'><a href='blog?pageNum=1'" + search + "><i class=\"material-icons\">first_page</i></a></li>");
        if (pageNum == 1) {
            pageCode.append("<li class=\"disabled\"><a><i class=\"material-icons\">chevron_left</i></a></li>");
        } else {
            pageCode.append("<li class='waves-effect'><a href=\"blog?pageNum=" + (pageNum - 1) + "&" + search + "\"><i class=\"material-icons\">chevron_left</i></a></li>");
        }
        for (int i = pageNum - 2; i <= pageNum + 2; i++) {
            if (i < 1 || i > totalPage) {
                continue;
            }
            if (i == pageNum) {
                pageCode.append("<li class='active waves-effect'><a href='#'>" + i
                        + "</a></li>");
            } else {
                pageCode.append("<li class='waves-effect'><a href='blog?pageNum=" + i + "'>" + i
                        + "</a></li>");
            }
        }
        if (pageNum == totalPage) {
            pageCode.append("<li class='disabled'><a><i class=\"material-icons\">chevron_right</i></a></li>");
        } else {
            pageCode.append("<li class='waves-effect'><a href='blog?pageNum=" + (pageNum + 1) + "&" + search
                    + "'><i class=\"material-icons\">chevron_right</i></a></li>");
        }
        pageCode.append("<li class='waves-effect'><a href='blog?pageNum=" + totalPage + "&" + search + "'><i class=\"material-icons\">last_page</i></a></li>");
        pageCode.append("</ul>");
        return pageCode.toString();
    }

    /**
     * 显示blog主要界面
     *
     * @param request
     * @param model
     * @param pageNum
     * @param search
     * @return
     */
    @RequestMapping(value = {"/blog", "/"}, method = RequestMethod.GET)
    public String showBlogView(HttpServletRequest request,
                               Model model,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "4") String pageSize,
                               @RequestParam(value = "search", required = false) String search) {
        //获取最近的文章
        List<ArticleLite> recentArticles = articleService.getRecentArticles();

        //获取浏览最多的文章
        List<ArticleLite> mostViewedArticles = articleService.getMostViewedArticles();

        //获取所有链接对象
        List<Link> links = linkService.getLinks();

        //将上述查询的对象添加到context中
        request.getServletContext().setAttribute("recentArticles", recentArticles);
        request.getServletContext().setAttribute("mostViewedArticles", mostViewedArticles);
        request.getServletContext().setAttribute("links", links);


        List<ArticleDto> articles = null;
        //翻页按钮
        String pageCode = null;

        if (search != null && !search.equals("")) {
            //根据关键字搜索
            articles = articleService.searchArticles(search);
            pageCode = pagingButton(articles.size(), Integer.parseInt(pageNum), Integer.parseInt(pageSize), "");
        } else {
            //根据条件查询文章分页结果 -- Paging 分页
            ServerResponse<PageInfo> articlesPage = articleService.getArticlesByPaging(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
            articles = articlesPage.getData().getList();
            //总记录数
            int total = (int) articlesPage.getData().getTotal();
            pageCode = pagingButton(total, Integer.parseInt(pageNum), Integer.parseInt(pageSize), search);


        }


        model.addAttribute("pageCode", pageCode);
        model.addAttribute("articles", articles);
        model.addAttribute("mainPage", "article.jsp");

        return "blog/blog";
    }

    @RequestMapping(value = "/blog/article/{articleId}", method = RequestMethod.GET)
    public String showArticleView(@PathVariable("articleId") Integer articleId, Model model, RedirectAttributes attributes) {
        //通过articleId拿到articleDto
        ServerResponse<ArticleDto> response = articleService.getArticleById(articleId);
        if (response.isSuccess()) {
            //增加文章点击次数
            ServerResponse serverResponse = articleService.addClick(articleId);
            if (!serverResponse.isSuccess()) {
                //增加文章点击数失败
                logger.error("add click error.\tarticle:%s", articleId);
            }
            //获得上一遍文章
            ArticleLite preArticle = articleService.getPreArticle(articleId);

            //获得下一篇文章
            ArticleLite nextArticle = articleService.getNextArticle(articleId);

            //把上一篇文章，下一篇文章，文章ID，文章内容添加到model中
            model.addAttribute("preArticle", preArticle);
            model.addAttribute("nextArticle", nextArticle);
            model.addAttribute("articleId", articleId);
            model.addAttribute("article", response.getData());
            model.addAttribute("mainPage", "articleDetail.jsp");

            return "blog/blog";
        } else {
            //为找到文章
            attributes.addFlashAttribute("info", response.getMsg());
            return "redirect:/blog";
        }
    }


    /**
     * 文章类别列表
     *
     * @param categoryId
     * @param model
     * @return
     */
    @RequestMapping(value = {"/blog/category", "/blog/category/{categoryId}"}, method = RequestMethod.GET)
    public String showCategoryView(@PathVariable("categoryId") Optional<Integer> categoryId, Model model) {
        List<Category> categories = categoryService.getCategories();
        HashMap<Integer, List<ArticleLite>> articleList = new HashMap<>();
        for (Category category : categories) {
            List<ArticleLite> articles = articleService.getArticlesByCategoryId(category.getCategoryId());
            articleList.put(category.getCategoryId(), articles);

        }
        if (categoryId.isPresent()) {
            model.addAttribute("categoryId", categoryId.get());
        } else {
            model.addAttribute("categoryId", "");
        }

        model.addAttribute("categories", categories);
        model.addAttribute("articlesList", articleList);
        model.addAttribute("mainPage", "category.jsp");

        return "blog/blog";

    }

    /**
     * 归档列表显示
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/blog/archive", method = RequestMethod.GET)
    public String showArchiveView(Model model) {
        List<ArticleDto> articles = articleService.getArticles();
        model.addAttribute("articles", articles);
        model.addAttribute("mainPage", "archive.jsp");
        return "blog/blog";
    }


    /**
     * 对消息的管理
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/blog/message", method = RequestMethod.GET)
    public String showMessageView(Model model) {
        model.addAttribute("articleId", 0);
        model.addAttribute("mainPage", "message.jsp");
        return "blog/blog";

    }

    /**
     * 关于页面的展示
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/blog/about", method = RequestMethod.GET)
    public String showAboutView(Model model) {
        AboutDto about = aboutService.getAbout();
        model.addAttribute("about", about);
        model.addAttribute("mainPage", "about.jsp");
        return "blog/blog";
    }


}
