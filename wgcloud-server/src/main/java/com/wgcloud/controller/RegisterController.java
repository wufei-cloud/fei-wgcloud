package com.wgcloud.controller;

import com.wgcloud.entity.LoginSet;
import com.wgcloud.entity.RegisterSet;
import com.wgcloud.service.RegisterServer;
import com.wgcloud.util.shorturl.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("Register")
public class RegisterController {

    @Resource
    private RegisterServer registerServer;


    @RequestMapping("toRegister")
    public String toRegister(Model model, HttpServletRequest request) throws Exception {
        return "register/register";
    }

    @RequestMapping(value = "register")
    public String Register(RegisterSet registerSet, Model model, HttpServletRequest request) throws Exception {
        String username = request.getParameter("userName");
        String Passwd = request.getParameter("Passwd");
        String PasswdAgain = request.getParameter("PasswdAgain");
        String Token = request.getParameter("Token");
        String token0 = "2c760240585a429697845f00c7704cad";
        String token1 = "91087cefa9644b5cb986de9a3ab72060";
        List<RegisterSet> list = registerServer.selectUserPass(username);
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(Passwd) && !StringUtils.isEmpty(PasswdAgain) && !StringUtils.isEmpty(Token)) {
            try {
                if (list.get(0) != null) {
                    model.addAttribute("error", "当前用户已存在，请更换用户后重试！");
                    return "register/register";
                }
            } catch (IndexOutOfBoundsException e) {
                if (MD5.GetMD5Code(Passwd.trim()).equals(MD5.GetMD5Code(PasswdAgain.trim()))) {
                    if (MD5.GetMD5Code(Token.trim()).equals(MD5.GetMD5Code(token0)) || MD5.GetMD5Code(Token.trim()).equals(MD5.GetMD5Code(token1))) {
                        if (MD5.GetMD5Code(Token.trim()).equals(MD5.GetMD5Code(token0))) {
                            registerSet.setUsername(username);
                            registerSet.setPassword(Passwd);
                            registerSet.setReghts_id("0");
                            registerServer.save(registerSet);
                        }else if(MD5.GetMD5Code(Token.trim()).equals(MD5.GetMD5Code(token1))){
                            registerSet.setUsername(username);
                            registerSet.setPassword(Passwd);
                            registerServer.save(registerSet);
                        }
                    } else {
                        model.addAttribute("error", "请联系管理员索要正确的凭证！");
                        return "register/register";
                    }
                } else {
                    model.addAttribute("error", "两次密码不一致，请重新输入！");
                    return "register/register";
                }

            }
        }

        return "redirect:/login/toLogin";
    }

}
