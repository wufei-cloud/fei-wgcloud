package com.wgcloud.controller;


import com.wgcloud.entity.DingSet;
import com.wgcloud.service.DingSetServer;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.staticvar.StaticKeys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2021.12.1
 */

@Controller
@RequestMapping("/dingset")
public class DingSetController {
    //    log
    private static final Logger logger = LoggerFactory.getLogger(DingSetController.class);

    @Resource
    private DingSetServer dingSetServer;
    @Resource
    private LogInfoService logInfoService;


    @RequestMapping("/list")
    public String DingSetList(DingSet Dingset, Model model, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        try {
            List<DingSet> list = dingSetServer.selectAllByParms(params);
            if (list.size() > 0) {
                model.addAttribute("dingSet", list.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = request.getParameter("msg");
        if (!StringUtils.isEmpty(msg)) {
            if (msg.equals("save")) {
                model.addAttribute("msg", "保存成功");
            }

        }

        return "dingding/view";
    }


    /**
     * 用于保存钉钉告警配置信息
     */

    @RequestMapping("save")
    public String saveDingSet(DingSet dingSet, Model model, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(dingSet.getId())) {
                dingSetServer.save(dingSet);
            } else {
                dingSetServer.updateById(dingSet);
            }
            StaticKeys.dingSet = dingSet;
        } catch (Exception e) {
            logger.error("保存钉钉设置信息错误：", e);
            logInfoService.save("钉钉告警信息设置失败", e.toString(), StaticKeys.LOG_ERROR);
        }

        return "redirect:/dingset/list?msg=save";
    }

}
