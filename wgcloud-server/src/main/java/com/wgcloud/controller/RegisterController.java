package com.wgcloud.controller;

import com.wgcloud.util.shorturl.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("Register")
public class RegisterController {
    @RequestMapping("toRegister")
    public String toRegister(Model model, HttpServletRequest request) throws  Exception {
        return "register/register";
    }

    @RequestMapping(value = "register")
    public String Register(Model model, HttpServletRequest request) throws  Exception {
        String Username = request.getParameter("userName");
        String Passwd = request.getParameter("Passwd");
        String PasswdAgain = request.getParameter("PasswdAgain");
        String Token = request.getParameter("Token");
        if (!StringUtils.isEmpty(Username) && !StringUtils.isEmpty(Passwd) && !StringUtils.isEmpty(PasswdAgain) && !StringUtils.isEmpty(Token)){
            System.out.println("登录成功");
        }

        return "redirect:/login/toLogin";
    }

}
