package chen.web;

import chen.common.ServerResponse;
import chen.dto.UserDto;
import chen.entity.User;
import chen.service.UserService;
import chen.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author bystander
 * @date 2018/8/1
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 控制台按钮
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showLoginView(HttpSession session){
        //判断用户是否登录
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser != null){
            return "redirect:/manage";
        }else{
            return "login/login";
        }
    }


    /**
     * 登录
     * @param request
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public String login(HttpServletRequest request, RedirectAttributes attributes){
        HttpSession session = request.getSession();
        ServerResponse response = userService.login(request.getParameter("username"), request.getParameter("password"));
        if (response.isSuccess()){
            UserDto userDto = (UserDto)response.getData();
            session.setAttribute("currentUser", userDto);
            if (userDto.getUserType() == 0){
                return "redirect:/blog";
            }else {
                return "redirect:/manage";
            }
        }else{
            attributes.addFlashAttribute("info", response.getMsg());
            return "redirect:/login";
        }

    }

}
