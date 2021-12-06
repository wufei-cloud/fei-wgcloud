package com.wgcloud.controller;

import com.wgcloud.entity.UserPass;
import com.wgcloud.service.UserPassServer;
import com.wgcloud.util.shorturl.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("Register")
public class RegisterController {

    @Resource
    private UserPassServer userPassServer;


    @RequestMapping("toRegister")
    public String toRegister(Model model, HttpServletRequest request) throws Exception {
        return "register/register";
    }

    @RequestMapping(value = "register")
    public String Register(UserPass userPass, Model model, HttpServletRequest request) throws Exception {
        String username = request.getParameter("userName");
        String password = request.getParameter("Passwd");
        String PasswdAgain = request.getParameter("PasswdAgain");
        String Token = request.getParameter("Token");
        List<UserPass> list = userPassServer.selectUserPass(username);
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(PasswdAgain) && !StringUtils.isEmpty(Token)) {
            try {
                if (list.get(0) != null) {
                    model.addAttribute("error", "当前用户已存在，请更换用户后重试！");
                    return "register/register";
                }
            } catch (IndexOutOfBoundsException e) {
                if (MD5.GetMD5Code(password).equals(MD5.GetMD5Code(PasswdAgain))) {
                    userPass.setUsername(username);
                    userPass.setPassword(password);
                    userPassServer.save(userPass);
                } else {
                    model.addAttribute("error", "两次密码不一致，请重新输入！");
                    return "register/register";
                }

            }
        }

        return "redirect:/login/toLogin";
    }

}
