package com.miaoxg.device.monitor.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.miaoxg.device.monitor.core.Role;
import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.service.UserService;
import com.miaoxg.device.monitor.util.JsonUtils;

@RestController
public class UserController extends AbstractController{
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Resource
    private UserService userService;
    
    /**
     * 身份认证/访问控制
     */
    @RequestMapping(value="auth", method=RequestMethod.POST)
    public String login(String username, String password, HttpSession session) {
        logger.debug("username = {}, password = {}", username, password);
        
        if(null == username || "".equals(username)){
             return JsonUtils.toFailureJson("请填写用户名");
        }
        if(null == password || "".equals(password)){
            return JsonUtils.toFailureJson("请填写密码");
        }
        
        User user = userService.login(username, password);
        session.setAttribute("user", user);
        return JsonUtils.toSuccessJson();
    }
    
    /**
     * 获取权限
     */
    @RequestMapping(value="role", method=RequestMethod.GET)
    public String getUserMenu(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null){
            return JsonUtils.toFailureJson("已过期，请重新登录");
        }
        return JsonUtils.toSuccessJson(user.getRole().name());
    }
    
    /**
     * 获取用户（分页）
     */
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public String getUsers(int iDisplayStart, int iDisplayLength, String sEcho) {
        logger.debug("get users form offset:{} and limit:{}", iDisplayStart, iDisplayLength);
        // 封装查询参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("offset", iDisplayStart);
        param.put("limit", iDisplayLength);

        Object[] resultArr = userService.getListAndTotalCount(param);
        int count = (int)resultArr[0];
        List<User> userList = (List<User>)resultArr[1];
        // 封装返回参数
        return JsonUtils.toDatatablesJson(count, count, sEcho, userList);
    }
}
