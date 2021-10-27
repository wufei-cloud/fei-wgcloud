package com.wgcloud.util.msg;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.DingTalk;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class WarnDingTalk {
    /*
    紧急告警 Q0  告警规则对应资源发生紧急故障，影响业务视为紧急告警。
    重要告警 Q1  告警规则对应资源存在影响业务的问题，此问题相对较严重，有可能会阻碍资源的正常使用。
    次要告警 Q2  告警规则对应资源存在相对不太严重点问题，此问题不会阻碍资源的正常使用。
    提示告警 Q3  告警规则对应资源存在潜在的错误可能影响到业务。
     */
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
            if (cpuState.getSys() >= 90){
                try {
                    String title = ("CPU告警");
                    String text = ("**<font color=#FF0000 size=6>CPU告警 Q1 </font>** \n\n @15344062110 发生的机器是:" + cpuState.getHostname() + "\n\n" + "当前CPU使用率为：" + cpuState.getSys() + "\n\n点击查看" + "http://192.168.75.135:9999/wgcloud/log/list");
                    sendDing(title,text);
                    logInfoService.save(title,text,StaticKeys.LOG_ERROR);
                }catch (Exception e){
                    logger.error("发送CPU告警信息失败：",e);
                    logInfoService.save("发送CPU告警失败",e.toString(),StaticKeys.LOG_ERROR);
                }
            }else {
                try {
                    String title = ("CPU告警");
                    String text = ("**<font color=#750000 size=4>CPU告警 Q2 </font>** \n\n @15344062110 发生的机器是:" + cpuState.getHostname() + "\n\n" + "当前CPU使用率为：" + cpuState.getSys() + "\n\n点击查看" + "http://192.168.75.135:9999/wgcloud/log/list");
                    sendDing(title,text);
                    logInfoService.save(title,text,StaticKeys.LOG_ERROR);
                }catch (Exception e){
                    logger.error("发送CPU告警信息失败：",e);
                    logInfoService.save("发送CPU告警失败",e.toString(),StaticKeys.LOG_ERROR);
                }
            }
        }
        return false;
    }

    public static boolean sendMemWarnInfo(MemState memState){
        if (memState.getUsePer() != null && memState.getUsePer() >= 70){
            if (memState.getUsePer() >= 90){
                try {
                    String title = ("内存告警");
                    String text = ("**<font color=#FF0000 size=6>内存告警 Q1 </font>** \n\n @15344062110 发生的机器是:" + memState.getHostname() + " \n\n " + "当前内存使用率为:" + memState.getUsePer() + "\n\n请尽快点击查看" + "http://192.168.75.135:9999/wgcloud/log/list");
                    sendDing(title,text);

                    logInfoService.save(title,text,StaticKeys.LOG_ERROR);
                }catch (Exception e){
                    logger.error("发送内存告警信息失败：",e);
                    logInfoService.save("发送内存告警失败",e.toString(),StaticKeys.LOG_ERROR);
                }
            }else {
                try {
                    String title = ("内存告警");
                    String text = ("**<font color=#750000 size=4>内存告警 Q2 </font>** \n\n @15344062110 发生的机器是:" + memState.getHostname() + " \n\n " + "当前内存使用率为：" + memState.getUsePer() + "\n\n点击查看" + "http://192.168.75.135:9999/wgcloud/log/list");
                    sendDing(title,text);
                    logInfoService.save(title,text,StaticKeys.LOG_ERROR);
                }catch (Exception e){
                    logger.error("发送内存告警信息失败：",e);
                    logInfoService.save("发送内存告警失败",e.toString(),StaticKeys.LOG_ERROR);
                }
            }
        }
        return false;

    }


    public static String sendDing(String title, String text) {

//        消息模板

        String textmsg = "{ \"msgtype\": \"markdown\",\n" +
                "     \"markdown\": {\n" +
                "         \"title\": \""+ title +"\" ,\n" +
                "         \"text\": \"" + text + "\" \n" +
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

