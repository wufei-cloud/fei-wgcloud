package com.wgcloud.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.*;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.MD5Utils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnDingTalk;
import com.wgcloud.util.shorturl.MD5;
import com.wgcloud.util.staticvar.BatchData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @version v2.3
 * @ClassName:AgentController.java
 * @author: http://www.wgstart.com
 * @date: 2019年11月16日
 * @Description: AgentController.java
 * @Copyright: 2017-2021 wgcloud. All rights reserved.
 */
@Controller
@RequestMapping("/agent")
public class AgentController {


    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 40, 2, TimeUnit.MINUTES, new LinkedBlockingDeque<>());


    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping("/minTask")
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject) JSONUtil.parse(paramBean);
        JSONObject resultJson = new JSONObject();
//go 客户端验证 wgToken
        if (!MD5Utils.GetMD5Code(agentJsonObject.getStr("wgToken")).equals(MD5Utils.GetMD5Code(commonConfig.getWgToken()))) {
            logger.error("token is invalidate");
            resultJson.put("result", "error：token is invalidate");
            return resultJson;
        }
//        java 客户端验证 wgToken
//        if (!tokenUtils.checkAgentToken(agentJsonObject)) {
//            logger.error("token is invalidate111");
//            resultJson.put("result", "error：token is invalidate");
//            return resultJson;
//        }
        JSONObject cpuState = agentJsonObject.getJSONObject("cpuState");
        JSONObject memState = agentJsonObject.getJSONObject("memState");
        JSONObject sysLoadState = agentJsonObject.getJSONObject("sysLoadState");
        JSONArray appInfoList = agentJsonObject.getJSONArray("appInfoList");   // 目前没有使用
        JSONArray appStateList = agentJsonObject.getJSONArray("appStateList"); // 目前没有使用
        JSONObject logInfo = agentJsonObject.getJSONObject("logInfo"); // 目前没有使用
        JSONObject systemInfo = agentJsonObject.getJSONObject("systemInfo");
        JSONObject netIoState = agentJsonObject.getJSONObject("netIoState");
        JSONArray deskStateList = agentJsonObject.getJSONArray("diskStateList");

        try {

            if (logInfo != null) {
                LogInfo bean = new LogInfo();
                BeanUtil.copyProperties(logInfo, bean);
                BatchData.LOG_INFO_LIST.add(bean);
            }
            if (cpuState != null) {
                CpuState bean = new CpuState();
                BeanUtil.copyProperties(cpuState, bean);
                BatchData.CPU_STATE_LIST.add(bean);
                Runnable runnable = () -> {
//                    WarnMailUtil.sendCpuWarnInfo(bean);
                    WarnDingTalk.sendCpuWarnInfo(bean);
                };
                executor.execute(runnable);

            }
            if (memState != null) {
                MemState bean = new MemState();
                BeanUtil.copyProperties(memState, bean);
                BatchData.MEM_STATE_LIST.add(bean);
                Runnable runnable = () -> {
//                    WarnMailUtil.sendWarnInfo(bean);
                    WarnDingTalk.sendMemWarnInfo(bean);
                };
                executor.execute(runnable);
            }
            if (sysLoadState != null) {
                SysLoadState bean = new SysLoadState();
                BeanUtil.copyProperties(sysLoadState, bean);
                BatchData.SYSLOAD_STATE_LIST.add(bean);
            }
            if (netIoState != null) {
                NetIoState bean = new NetIoState();
                BeanUtil.copyProperties(netIoState, bean);
                BatchData.NETIO_STATE_LIST.add(bean);
                Runnable runnable = () -> {
                  WarnDingTalk.netMonitor(bean);
                };
                executor.execute(runnable);
            }
            if (appInfoList != null && appStateList != null) {
                List<AppInfo> appInfoResList = JSONUtil.toList(appInfoList, AppInfo.class);
                for (AppInfo appInfo : appInfoResList) {
                    BatchData.APP_INFO_LIST.add(appInfo);
                }
                List<AppState> appStateResList = JSONUtil.toList(appStateList, AppState.class);
                for (AppState appState : appStateResList) {
                    BatchData.APP_STATE_LIST.add(appState);
                }
            }
            if (systemInfo != null) {
                SystemInfo bean = new SystemInfo();
                BeanUtil.copyProperties(systemInfo, bean);
                BatchData.SYSTEM_INFO_LIST.add(bean);
            }
            if (deskStateList != null) {
                for (Object jsonObjects : deskStateList) {
                    DeskState bean = new DeskState();
                    BeanUtil.copyProperties(jsonObjects, bean);
                    BatchData.DESK_STATE_LIST.add(bean);
                }
            }
            resultJson.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultJson.put("result", "error：" + e.toString());
        } finally {
            return resultJson;
        }
    }

}
