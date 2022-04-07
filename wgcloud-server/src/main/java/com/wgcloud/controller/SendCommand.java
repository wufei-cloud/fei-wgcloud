package com.wgcloud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/command")
public class SendCommand {
//    log
    private static final Logger logger = LoggerFactory.getLogger(SendCommand.class);

    @RequestMapping("/list")
    public String CmdSend(Model model, HttpServletRequest request){
        model.addAttribute("command");

        return "sendcmd/view";
    }
}
