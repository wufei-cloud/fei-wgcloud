package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.LoginSet;
import com.wgcloud.service.LoginServer;
import com.wgcloud.util.passwdMD5.MD5PassWd;
import com.wgcloud.util.shorturl.MD5;
import com.wgcloud.util.staticvar.StaticKeys;
import net.sf.jsqlparser.statement.create.table.Index;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @version v2.3
 * @ClassName:LoginCotroller.java
 * @author: http://www.wgstart.com
 * @date: 2019年11月16日
 * @Description: LoginCotroller.java
 * @Copyright: 2017-2021 wgcloud. All rights reserved.
 */
@Controller
@RequestMapping(value = "/login")
public class LoginCotroller {

    private static final Logger logger = LoggerFactory.getLogger(LoginCotroller.class);

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private LoginServer loginServer;

    /**
     * 转向到登录页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("toLogin")
    public String toLogin(Model model, HttpServletRequest request) {
        return "login/login";
    }

    /**
     * 登出系统
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("loginOut")
    public String loginOut(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/login/toLogin";
    }

    /**
     * 管理员登录验证
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "login")
    public String login(Model model, HttpServletRequest request) throws Exception {
        Date now=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("YYY-MM-dd HH:mm:ss");//格式化时间显示
        String userName = request.getParameter("userName").trim();
        String passwd = request.getParameter("md5pwd").trim();
        String code = request.getParameter(StaticKeys.SESSION_CODE);

        HttpSession session = request.getSession();
        List<LoginSet> list = loginServer.selectUserPass(userName);
        try {
            LoginSet DBUser = list.get(0);
            if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(passwd) && !StringUtils.isEmpty(code)) {
                if (!code.equals(session.getAttribute(StaticKeys.SESSION_CODE))) {
                    model.addAttribute("error", "验证码错误");
                    return "login/login";
                }
                AccountInfo accountInfo = new AccountInfo();
                if (MD5PassWd.md5MinusSalt(DBUser.getPassword().trim()).equals(passwd) && (DBUser.getUsername().trim()).equals(userName)) {
                    accountInfo.setAccount(DBUser.getUsername());
                    accountInfo.setId(DBUser.getUsername());
                    request.getSession().setAttribute(StaticKeys.LOGIN_KEY, accountInfo);
                    session.setAttribute("userName", userName);
                    logger.info("用户 "+userName+" 在 "+sdf.format(now)+" 登录成功，登录的IP是: "+request.getHeader("x-forwarded-for"));
                    return "redirect:/dash/main";
                }
            }
        } catch (IndexOutOfBoundsException e) {
            model.addAttribute("error", "当前用户不存在！");
            return "login/login";
        } catch (Exception e) {
            logger.error("登录异常：", e);
        }
        model.addAttribute("error", "帐号或者密码错误");
        logger.warn(request.getHeader("x-forwarded-for")+" 在 "+sdf.format(now)+" 使用用户 "+userName+" 尝试登录失败！！！");
        return "login/login";
    }


}