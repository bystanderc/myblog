package chen.interceptor;

import chen.dto.UserDto;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bystander
 * @date 2018/7/31
 *
 * 登录拦截器
 */

public class LoginInterceptor implements HandlerInterceptor {
    //如果用户为登录，拦截用户的请求，并强制转发到首页
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserDto user = (UserDto)session.getAttribute("currentUser");
        if (user != null){
            return true;
        }
        response.sendRedirect("/blog");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
