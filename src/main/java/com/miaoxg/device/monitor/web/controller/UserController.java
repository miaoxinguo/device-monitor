package com.miaoxg.device.monitor.web.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.miaoxg.device.monitor.service.UserService;
import com.miaoxg.device.monitor.util.JsonUtils;

@RestController
public class UserController extends AbstractController{
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Resource
    private UserService userService;
    
    @RequestMapping(value="auth", method=RequestMethod.POST)
    public String login(String username, String password, HttpSession session) {
        logger.debug("username = {}, password = {}", username, password);
        
        
        // TODO 暂时写死用户名/口令，以后从数据库判断
        if(!"admin".equals(username) && !"demo".equals(username)){
             return JsonUtils.toFailureJson("用户名不存在");
        }
        
        // 验证密码
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            return JsonUtils.toFailureJson("系统异常");
        }
        messageDigest.update("123456".getBytes());
        
        String passwordHex = Hex.encodeHexString(messageDigest.digest());
        if(!passwordHex.equals(password)){
            return JsonUtils.toFailureJson("密码错误");
        }
        session.setAttribute("isAdmin", "admin".equals(username));
        return JsonUtils.toSuccessJson();
    }
    
    /**
     * 获取权限
     */
    @RequestMapping(value="menu", method=RequestMethod.GET)
    public String getUserMenu(HttpSession session) {
        // TODO 临时方式
        boolean isAdmin = Boolean.parseBoolean(session.getAttribute("isAdmin").toString());
        return JsonUtils.toSuccessJson(isAdmin ? "1" : "0");
    }
}
