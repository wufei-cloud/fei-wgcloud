package com.wgcloud.util.msg;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.DingTalk;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.entity.MemState;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;

public class WarnDingTalk {
    @Autowired
    private  RestUtil restUtil;
//   输出log
    private static Logger logger = (Logger) LoggerFactory.getLogger(WarnDingTalk.class);
//    保存log
    private static LogInfoService logInfoService = (LogInfoService) ApplicationContextHelper.getBean(LogInfoService.class);
//   DingDing config
    private static DingTalk dingTalk = (DingTalk) ApplicationContextHelper.getBean(DingTalk.class);

    public static boolean sendCpuWarnInfo(CpuState cpuState) {
        if (cpuState.getSys() != null && cpuState.getSys() >= 70) {
            dingTalk.setTitle("L2 告警");
            dingTalk.setText("发生的机器是:" + cpuState.getHostname() + "\n" + "当前CPU使用率为：" + cpuState.getSys() + "点击查看" + "http://192.168.75.135:9999/wgcloud/log/list");
            try {
                sendDing(dingTalk);
                logInfoService.save(dingTalk.getTitle(),dingTalk.getText(),StaticKeys.LOG_ERROR);
            }catch (Exception e){
                logger.error("发送CPU告警信息失败：",e);
                logInfoService.save("发送CPU告警失败",e.toString(),StaticKeys.LOG_ERROR);
            }
        }
        return false;
    }

    public static boolean sendMemWarnInfo(MemState memState){
        if (memState.getUsePer() != null && memState.getUsePer() >= 70){
            dingTalk.setTitle("L2 告警");
            dingTalk.setText("发生的机器是:" + memState.getHostname() + "\n" + "当前内存使用率为：" + memState.getUsePer() + "点击查看" + "http://192.168.75.135:9999/wgcloud/log/list");
            try {
                sendDing(dingTalk);
                logInfoService.save(dingTalk.getTitle(),dingTalk.getText(),StaticKeys.LOG_ERROR);
            }catch (Exception e){
                logger.error("发送内存告警信息失败：",e);
                logInfoService.save("发送内存告警失败",e.toString(),StaticKeys.LOG_ERROR);
            }
        }
        return false;

    }

    public static String sendDing(DingTalk dingTalk) {

//        消息模板

        String textmsg = "{ \"msgtype\": \"markdown\",\n" +
                "     \"markdown\": {\n" +
                "         \"title\":\"" + dingTalk.getTitle() + "\",\n" +
                "         \"text\": \"" + dingTalk.getText() + "\" \n" +
                "     },\n" +
                "      \"at\": {\n" +
                "          \"atMobiles\": [\n" +
                "              \"15344062110\"\n" +
                "          ],\n" +
                "          \"atUserIds\": [\n" +
                "              \"15344062110\"\n" +
                "          ],\n" +
                "          \"isAtAll\": false\n" +
                "      }}";
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://oapi.dingtalk.com/robot/send?access_token=e097866b2b35e774dcaf1087235ed986392bd4ecf42d1091bc1070063867dd95");
            httpPost.setHeader("Content-type", "application/json;utf-8");
            StringEntity stringEntity = new StringEntity(textmsg, "utf-8");
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
//            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                System.out.println(EntityUtils.toString(httpResponse.getEntity(), "utf-8"));
//            }
            return "send success";
        } catch (Exception e) {
            logger.error("DingDing发送错误：",e);
            logInfoService.save("发送DingDing错误，",e.toString(),StaticKeys.LOG_ERROR);
            return "send error";
        }
    }

}

