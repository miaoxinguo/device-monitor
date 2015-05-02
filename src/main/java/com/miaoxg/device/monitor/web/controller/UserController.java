package com.miaoxg.device.monitor.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.miaoxg.device.monitor.entity.User;
import com.miaoxg.device.monitor.service.UserService;
import com.miaoxg.device.monitor.util.JsonUtils;
import com.miaoxg.device.monitor.vo.DataTablesVo;
import com.miaoxg.device.monitor.vo.UserVo;

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
     * 新增一个用户
     */
    @RequestMapping(value="user", method=RequestMethod.POST)
    public String addUser(User user) {
        logger.debug("新用户:{}", user.getName());
        logger.debug("用户管理的酒店有:{}个", user.getHotels().size());
        
        if(userService.existUser(user.getName())){
            return JsonUtils.toSuccessJson("用户名已存在");
        }
        userService.addUser(user);
        
        // 封装返回参数
        return JsonUtils.toSuccessJson();
    }
   
    /**
     * 删除一个用户
     */
    @RequestMapping(value="user/{id}", method=RequestMethod.DELETE)
    public String addUser(@PathVariable Integer id) {
        userService.removeUser(id);
        
        // 封装返回参数
        return JsonUtils.toSuccessJson();
    }
    
    /**
     * 修改用户信息
     */
    @RequestMapping(value="user/{id}", method=RequestMethod.PUT)
    public String modifyUser(User user, @PathVariable Integer id) {
        if(id < 0){
            return JsonUtils.toFailureJson("请选择一个用户");
        }
        userService.modifyUser(user);
        
        // 封装返回参数
        return JsonUtils.toSuccessJson();
    }
    
    /**
     * 修改密码
     */
    @RequestMapping(value="password", method = RequestMethod.PUT)
    public String changePassword(String oldPw, String newPw, HttpSession session) {
        User user = (User)session.getAttribute("user");
        userService.modifyPassword(user.getName(), newPw, oldPw);
        
        // 封装返回参数
        return JsonUtils.toSuccessJson();
    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value="user/{id}", method=RequestMethod.GET)
    public String getUserInfo(@PathVariable Integer id) {
        // 封装返回参数
        return JsonUtils.toJsonString(userService.getUserDetail(id));
    }
    
    /**
     * 获取用户（分页）
     */
    @RequestMapping(value="users", method = RequestMethod.GET)
    public String getUsers(UserVo vo) {
        logger.debug("get users form offset:{} and limit:{}", vo.getiDisplayStart(), vo.getiDisplayLength());

        DataTablesVo<User> dtvo = userService.getUsers(vo);
        int count = dtvo.getCount();
        List<User> userList = dtvo.getList();
        // 封装返回参数
        return JsonUtils.toDatatablesJson(count, count, vo.getsEcho(), userList);
    }
    
    /**
     * 维保人员名
     */
    @RequestMapping(value="maintainerNames", method=RequestMethod.GET)
    public String getMaintainerNames() {
        return JsonUtils.toJsonString(userService.getMaintainerNames());
    }
}
