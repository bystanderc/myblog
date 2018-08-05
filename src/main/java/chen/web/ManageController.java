package chen.web;

import chen.common.ServerResponse;
import chen.dto.*;
import chen.entity.Article;
import chen.entity.Category;
import chen.entity.Link;
import chen.entity.User;
import chen.service.*;
import chen.util.ImageUtil;
import chen.util.MD5Util;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author bystander
 * @date 2018/8/3
 */
@Controller
@RequestMapping("/manage")
public class ManageController {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ManageController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AboutService aboutService;

    @Autowired
    private LinkService linkService;


    @RequestMapping(method = RequestMethod.GET)
    public String showManageView(Model model) {
        model.addAttribute("mainPage", "manageView.jsp");
        return "manage/manage";
    }

    /**
     * 对文章的管理
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public String article(Model model) {
        //获取所有文章
        List<ArticleDto> articles = articleService.getArticles();
        model.addAttribute("articles", articles);
        model.addAttribute("mainPage", "article.jsp");
        return "manage/manage";
    }

    /**
     * 写文章或者编辑文章
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/article/write", "/article/modify/{articleId}"}, method = RequestMethod.GET)
    public String editArticle(@PathVariable("articleId") Optional<Integer> articleId, Model model, RedirectAttributes attributes, HttpServletRequest request) {

        if (articleId.isPresent()) {
            //文章ID存在，编辑文章
            ServerResponse<ArticleDto> response = articleService.getArticleById(articleId.get());
            if (response.isSuccess()) {
                model.addAttribute("article", response.getData());
            } else {
                attributes.addFlashAttribute("info", response.getMsg());
                return "manage/article";
            }

        }
        List<Category> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        //获取图片
        String[] images = ImageUtil.getImages(request.getServletContext().getRealPath("images/article"));
        model.addAttribute("images", images);

        model.addAttribute("mainPage", "articleEditor.jsp");
        return "manage/manage";
    }

    /**
     * 保存文章
     *
     * @param articleId
     * @param request
     * @param attributes
     * @return
     */
    @RequestMapping(value = {"/article/save", "/article/save/{articleId}"}, method = RequestMethod.POST)
    public String saveArticle(@PathVariable("articleId") Optional<Integer> articleId, HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {

        UserDto user = (UserDto) session.getAttribute("currentUser");
        String title = request.getParameter("title");
        String categoryId = request.getParameter("categoryId");
        String content = request.getParameter("content");
        String image = request.getParameter("image");

        Article article = new Article(Integer.parseInt(categoryId), title, content, image);

        ServerResponse res = null;
        if (articleId.isPresent()) {
            //更新文章

            ServerResponse<ArticleDto> response = articleService.getArticleById(articleId.get());
            article.setClicks(response.getData().getClicks());
            article.setArticleId(articleId.get());
            res = articleService.updateArticle(article);
        } else {
            //新写文章
            article.setPubDate(new Date(System.currentTimeMillis()));
            article.setUserId(user.getUserId());
            res = articleService.saveArticle(article);
        }
        attributes.addFlashAttribute("info", res.getMsg());
        return "redirect:/manage/article";
    }

    /**
     * 删除文章
     *
     * @param articleId
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/article/delete/{articleId}", method = RequestMethod.GET)
    public String deleteArticle(@PathVariable("articleId") Integer articleId, RedirectAttributes attributes, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("currentUser");
        if (user.getUserType() == 0) {
            attributes.addFlashAttribute("info", "不是管理员，无权删除文章");
        } else {
            ServerResponse response = articleService.deleteArticle(articleId);
            attributes.addFlashAttribute("info", response.getMsg());
        }
        return "redirect:/manage/article";
    }


    /**
     * 显示分类管理界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String manageCategory(Model model) {
        //获取所有分类
        List<Category> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("mainPage", "category.jsp");
        return "manage/manage";
    }


    /**
     * 增加或修改分类
     *
     * @param categoryId
     * @param model
     * @param attributes
     * @return
     */
    @RequestMapping(value = {"/category/add", "/category/modify/{categoryId}"}, method = RequestMethod.GET)
    public String editCategory(@PathVariable("categoryId") Optional<Integer> categoryId, Model model, RedirectAttributes attributes) {

        if (categoryId.isPresent()) {
            ServerResponse<CategoryDto> response = categoryService.getCategoryById(categoryId.get());
            if (response.isSuccess()) {
                model.addAttribute("category", response.getData());
            } else {
                //为查询到分类
                attributes.addFlashAttribute("info", response.getMsg());
                return "redirect:/manage/category";
            }
        }
        model.addAttribute("mainPage", "categoryEditor.jsp");
        return "/manage/manage";
    }


    /**
     * 保存分类
     *
     * @param categoryId
     * @param attributes
     * @param request
     * @return
     */
    @RequestMapping(value = {"/category/save", "/category/save/{categoryId}"}, method = RequestMethod.POST)
    public String saveCategory(@PathVariable("categoryId") Optional<Integer> categoryId, RedirectAttributes attributes, HttpServletRequest request) {
        String categoryName = request.getParameter("categoryName");
        ServerResponse res = null;
        if (categoryId.isPresent()) {
            res = categoryService.updateCategory(new CategoryDto(categoryId.get(), categoryName));
        } else {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryName(categoryName);
            //添加分类
            res = categoryService.saveCategory(categoryDto);
        }
        attributes.addFlashAttribute("info", res.getMsg());
        return "redirect:/manage/category";

    }

    /**
     * 删除分类
     *
     * @param categoryId
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/category/delete/{categoryId}", method = RequestMethod.GET)
    public String deleteCategory(@PathVariable("categoryId") Integer categoryId, RedirectAttributes attributes, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("currentUser");
        if (user.getUserType() == 0) {
            attributes.addFlashAttribute("info", "不是管理员，无权删除分类");
        } else {
            ServerResponse response = categoryService.deleteCategoryById(categoryId);
            attributes.addFlashAttribute("info", response.getMsg());
        }
        return "redirect:/manage/category";
    }

    /**
     * 对链接的管理
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/link", method = RequestMethod.GET)
    public String manageLink(Model model) {
        List<Link> links = linkService.getLinks();
        model.addAttribute("links", links);
        model.addAttribute("mainPage", "link.jsp");
        return "/manage/manage";
    }

    /**
     * 修改/添加链接
     *
     * @param linkId
     * @param model
     * @param attributes
     * @return
     */
    @RequestMapping(value = {"/link/add", "/link/modify/{linkId}"}, method = RequestMethod.GET)
    public String editLink(@PathVariable("linkId") Optional<Integer> linkId, Model model, RedirectAttributes attributes, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("currentUser");
        if (user.getUserType() == 0) {
            attributes.addFlashAttribute("info", "不是管理员，无权操作");
            return "redirect:/manage/link";
        } else {
            if (linkId.isPresent()) {
                ServerResponse result = linkService.getlinkById(linkId.get());
                if (result.isSuccess()) {
                    model.addAttribute("link", result.getData());
                }
                attributes.addFlashAttribute("info", result.getMsg());
            }
            model.addAttribute("mainPage", "linkEditor.jsp");
            return "manage/manage";
        }
    }

    /**
     * 保存用户
     * @param linkId
     * @param request
     * @param attributes
     * @return
     */
    @RequestMapping(value = {"/link/save", "/link/save/{linkId}"}, method = RequestMethod.POST)
    public String saveLink(@PathVariable("linkId") Optional<Integer> linkId, HttpServletRequest
            request, RedirectAttributes attributes) {
        String linkName = request.getParameter("linkName");
        String url = request.getParameter("url");
        ServerResponse res = null;
        if (linkId.isPresent()) {
            //保存修改的链接
            LinkDto linkDto = new LinkDto(linkId.get(), linkName, url);
            res = linkService.updateLink(linkDto);
        } else {
            LinkDto link = new LinkDto(linkName, url);
            res = linkService.saveLink(link);

        }
        attributes.addFlashAttribute("info", res.getMsg());
        return "redirect:/manage/link";
    }


    /**
     * 删除链接
     *
     * @param linkId
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/link/delete/{linkId}", method = RequestMethod.GET)
    public String deleteLink(@PathVariable("linkId") Integer linkId, RedirectAttributes attributes, HttpSession
            session) {
        UserDto user = (UserDto) session.getAttribute("currentUser");
        if (user.getUserType() == 0) {
            attributes.addFlashAttribute("info", "不是管理员，无权操作");
        } else {
            ServerResponse res = linkService.deleteLink(linkId);
            attributes.addFlashAttribute("info", res.getMsg());
        }
        return "redirect:/manage/link";
    }


    /**
     * 用户管理界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String manageUser(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("users", userList);
        model.addAttribute("mainPage", "user.jsp");
        return "manage/manage";

    }

    /**
     * 注册用户/修改用户
     *
     * @param userId
     * @param model
     * @param request
     * @param attributes
     * @return
     */
    @RequestMapping(value = {"/user/register", "/user/modify/{userId}"}, method = RequestMethod.GET)
    public String register(@PathVariable("userId") Optional<Integer> userId, Model model, HttpServletRequest
            request, RedirectAttributes attributes, HttpSession session) {
        //首先判断用户权限 ，如果不是管理员，不允许修改或者增加用户
        UserDto currentUser = (UserDto) session.getAttribute("currentUser");
        if (currentUser.getUserType() == 0) {
            attributes.addFlashAttribute("info", "不是管理员，没有权限操作");
            return "redirect:/manage/user";
        }
        if (userId.isPresent()) {
            ServerResponse response = userService.getUserById(userId.get());
            if (response.isSuccess()) {
                model.addAttribute("user", response.getData());
            } else {
                attributes.addFlashAttribute("info", response.getMsg());
            }

        }

        String[] images = ImageUtil.getImages(request.getServletContext().getRealPath("images/user"));
        model.addAttribute("images", images);
        model.addAttribute("mainPage", "userEditor.jsp");
        return "manage/manage";

    }

    /**
     * 删除用户
     *
     * @param userId
     * @param attributes
     * @param session
     * @return
     */
    @RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("userId") Integer userId, RedirectAttributes attributes, HttpSession
            session) {
        UserDto currentUser = (UserDto) session.getAttribute("currentUser");
        if (currentUser.getUserType() == 0) {
            attributes.addFlashAttribute("info", "不是管理员，没有权限操作");
            return "redirect:/manage/user";
        }
        ServerResponse response = userService.deleteUser(userId);
        attributes.addFlashAttribute("info", response.getMsg());
        return "redirect:/manage/user";
    }

    /**
     * 保存用户
     *
     * @param userId
     * @param request
     * @param attributes
     * @param session
     * @return
     */
    @RequestMapping(value = {"/user/save", "/user/save/{userId}"}, method = RequestMethod.POST)
    public String saveUser(@PathVariable("userId") Optional<Integer> userId, HttpServletRequest
            request, RedirectAttributes attributes, HttpSession session) {
        UserDto currentUser = (UserDto) session.getAttribute("currentUser");
        if (currentUser.getUserType() == 0) {
            attributes.addFlashAttribute("info", "不是管理员，没有权限操作");
            return "redirect:/manage/user";
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String image = request.getParameter("image");
        ServerResponse res = null;
        if (userId.isPresent()) {
            //更新用户
            res = userService.updateUser(new User(userId.get(), username, MD5Util.MD5EncodeUtf8(password), image));

        } else {
            res = userService.register(new User(username, MD5Util.MD5EncodeUtf8(password), image));
        }
        attributes.addFlashAttribute("info", res.getMsg());
        return "redirect:/manage/user";
    }

    /**
     * 对图片的管理
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/image")
    public String manageImage(HttpServletRequest request, Model model) {

        String baseImage = null;

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"


        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;


            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();

            String path = null;

            while (iter.hasNext()) {
                //一次遍历所有文件
                CommonsMultipartFile file = (CommonsMultipartFile) multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    //上传
                    try {
                        if (file.getFileItem().getFieldName().equals("userImage")) {
                            path = request.getServletContext().getRealPath("images/user") + File.separator + file.getOriginalFilename();
                        } else if (file.getFileItem().getFieldName().equals("articleImage")) {
                            path = request.getServletContext().getRealPath("images/article") + File.separator + file.getOriginalFilename();
                        }
                        file.transferTo(new File(path));
                        logger.info("上传图片成功");
                    } catch (IOException e) {
                        logger.error("上传文件失败", e);
                    }
                }

            }

        }


        String[] articleImages = ImageUtil.getImages(request.getServletContext().getRealPath("images/article"));
        String[] userImages = ImageUtil.getImages(request.getServletContext().getRealPath("images/user"));
        model.addAttribute("userImages", userImages);
        model.addAttribute("articleImages", articleImages);

        model.addAttribute("mainPage", "image.jsp");
        return "/manage/manage";
    }


    /**
     * 对于关于的管理
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String manageAbout(Model model) {
        AboutDto about = aboutService.getAbout();
        model.addAttribute("about", about);
        model.addAttribute("mainPage", "about.jsp");
        return "/manage/manage";
    }


    /**
     * 修改关于
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/about/modify", method = RequestMethod.GET)
    public String modifyAbout(Model model) {
        model.addAttribute("about", aboutService.getAbout());
        model.addAttribute("mainPage", "aboutEditor.jsp");
        return "/manage/manage";

    }


    /**
     * 保存关于
     *
     * @param request
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/about/save", method = RequestMethod.POST)
    public String saveAbout(HttpServletRequest request, RedirectAttributes attributes) {
        String content = request.getParameter("content");
        ServerResponse response = aboutService.updateAbout(content);
        attributes.addFlashAttribute("info", response.getMsg());
        return "redirect:/manage/about";
    }

    /**
     * 退出后台管理
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/quit", method = RequestMethod.GET)
    public String quit(HttpSession session) {
        session.invalidate();
        return "redirect:/blog";
    }
}
