package com.wgcloud.util.msg;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.entity.*;
import com.wgcloud.service.DingSetServer;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.FormatUtil;
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
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WarnDingTalk {
    /*
    紧急告警 Q0  告警规则对应资源发生紧急故障，影响业务视为紧急告警。
    重要告警 Q1  告警规则对应资源存在影响业务的问题，此问题相对较严重，有可能会阻碍资源的正常使用。
    次要告警 Q2  告警规则对应资源存在相对不太严重点问题，此问题不会阻碍资源的正常使用。
    提示告警 Q3  告警规则对应资源存在潜在的错误可能影响到业务。
     */
    @Autowired
    private RestUtil restUtil;
    //   输出log
    private static Logger logger = (Logger) LoggerFactory.getLogger(WarnDingTalk.class);
    //    保存log
    private static LogInfoService logInfoService = (LogInfoService) ApplicationContextHelper.getBean(LogInfoService.class);

    @Resource
    private DingSetServer dingSetServer;

    private static String Phone;

    private static String DingdingAPI;

    /**
     * 监控CPU 大于 90 Q1   大于 70 Q2
     *
     * @param cpuState
     * @return
     */
    public static boolean sendCpuWarnInfo(CpuState cpuState) {
        if (cpuState.getSys() != null && cpuState.getSys() >= 70) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cpuState.getSys() >= 90) {
                try {
                    String title = ("CPU告警");
                    String text = ("**<font color=#FF0000 size=6>CPU告警 Q1 </font>** \n\n " + getPhone()
                            + "发生的机器是:" +
                            cpuState.getHostname() + "\n\n" + "当前CPU使用率为：" + cpuState.getSys() + " %"
                            + "\n\n 事件发生的时间:" + date.format(cpuState.getCreateTime()));
                    sendDing(title, text);
                    logInfoService.save(title, text, StaticKeys.LOG_ERROR);
                } catch (Exception e) {
                    logger.error("发送CPU告警信息失败：", e);
                    logInfoService.save("发送CPU告警失败", e.toString(), StaticKeys.LOG_ERROR);
                }
            } else {
                try {
                    String title = ("CPU告警");
                    String text = ("**<font color=#750000 size=4>CPU告警 Q2 </font>** \n\n " + getPhone()
                            + " 发生的机器是:" +
                            cpuState.getHostname() + "\n\n" + "当前CPU使用率为：" + cpuState.getSys() + " %"
                            + "\n\n 事件发生的时间:" + date.format(cpuState.getCreateTime()));
                    sendDing(title, text);
                    logInfoService.save(title, text, StaticKeys.LOG_ERROR);
                } catch (Exception e) {
                    logger.error("发送CPU告警信息失败：", e);
                    logInfoService.save("发送CPU告警失败", e.toString(), StaticKeys.LOG_ERROR);
                }
            }
        }
        return false;
    }

    /**
     * 监控内存 大于 90 Q1   大于 70 Q2
     *
     * @param memState
     * @return
     */
    public static boolean sendMemWarnInfo(MemState memState) {
        if (memState.getUsePer() != null && memState.getUsePer() >= 70) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (memState.getUsePer() >= 90) {
                try {
                    String title = ("内存告警");
                    String text = ("**<font color=#FF0000 size=6>内存告警 Q1 </font>** \n\n " + getPhone()
                            + "\n\n发生的机器是:" + memState.getHostname() + " \n\n " + "当前内存使用率为:"
                            + memState.getUsePer() + " %"
                            + "\n\n 事件发生的时间:" + date.format(memState.getCreateTime()));
                    sendDing(title, text);

                    logInfoService.save(title, text, StaticKeys.LOG_ERROR);
                } catch (Exception e) {
                    logger.error("发送内存告警信息失败：", e);
                    logInfoService.save("发送内存告警失败", e.toString(), StaticKeys.LOG_ERROR);
                }
            } else {
                try {
                    String title = ("内存告警");
                    String text = ("**<font color=#750000 size=4>内存告警 Q2 </font>** \n\n " + getPhone() + " 发生的机器是:"
                            + memState.getHostname() + " \n\n " + "当前内存使用率为：" + memState.getUsePer() + " %"
                            + "\n\n 事件发生的时间:" + date.format(memState.getCreateTime()));
                    sendDing(title, text);
                    logInfoService.save(title, text, StaticKeys.LOG_ERROR);
                } catch (Exception e) {
                    logger.error("发送内存告警信息失败：", e);
                    logInfoService.save("发送内存告警失败", e.toString(), StaticKeys.LOG_ERROR);
                }
            }
        }
        return false;

    }

    /**
     * 主机下线检测
     *
     * @param systemInfo
     * @param isDown
     * @return
     */
    public static boolean sendHostDown(SystemInfo systemInfo, boolean isDown) {
        String key = systemInfo.getId();
        if (isDown) {
            if (!StringUtils.isEmpty(WarnPools.MEM_WARN_MAP.get(key))) {
                return false;
            }
            try {
                String title = ("主机下线告警 " + systemInfo.getHostname());
                String text = ("**<font color=#FF0000 size=6>主机下线告警 Q1 </font>** \n\n " + getPhone()
                        + " \n\n " +
                        "主机超过2分钟未上报数据，可能已经下线 " + systemInfo.getHostname() + "\n\n 主机备注: "
                        + systemInfo.getHostRemark() + "。 \n\n 如果不在监控该主机，请从主机列表移除同时不在接收该主机告警");
                sendDing(title, text);
                WarnPools.MEM_WARN_MAP.put(key, "1");
                logInfoService.save(title, text, StaticKeys.LOG_ERROR);
            } catch (Exception e) {
                logger.error("主机下线告警发送失败", e);
                logInfoService.save("主机下线告警发送失败", e.toString(), StaticKeys.LOG_ERROR);
            }
        } else {
            WarnPools.MEM_WARN_MAP.remove(key);
            try {
                String title = ("主机上线告警恢复 " + systemInfo.getHostname());
                String text = ("主机上线恢复 " + systemInfo.getHostname() + "\n\n 主机备注: "
                        + systemInfo.getHostRemark() + "。");
                sendDing(title, text);
//                WarnPools.MEM_WARN_MAP.put(key, "1");
                logInfoService.save(title, text, StaticKeys.LOG_ERROR);
            } catch (Exception e) {
                logger.error("主机上线恢复信息发送失败", e);
                logInfoService.save("主机上线恢复信息发送失败", e.toString(), StaticKeys.LOG_ERROR);
            }
        }
        return false;
    }

    /**
     * 进程下线检测
     *
     * @param appInfo
     * @param isDown
     * @return
     */
    public static boolean sendAppDown(AppInfo appInfo, boolean isDown) {
        String key = appInfo.getId();
        if (isDown) {
            if (!StringUtils.isEmpty(WarnPools.MEM_WARN_MAP.get(key))) {
                return false;
            }
            try {
                String title = ("进程下线告警 " + appInfo.getHostname());
                String text = ("**<font color=#FF0000 size=6>进程下线告警 Q1 </font>** " + getPhone()
                        + " \n\n" +
                        " 进程超过十分钟未上报数据，可能已经下线 " + appInfo.getHostname() + "\n\n "
                        + appInfo.getAppName() + "。 \n\n 如果不在监控该进程，请从进程列表移除同时不在接收该进程告警");
                sendDing(title, text);
                WarnPools.MEM_WARN_MAP.put(key, "1");
                logInfoService.save(title, text, StaticKeys.LOG_ERROR);
            } catch (Exception e) {
                logger.error("发送进程下线告警邮件失败：", e);
                logInfoService.save("发送进程下线告警错误", e.toString(), StaticKeys.LOG_ERROR);
            }
        } else {
            WarnPools.MEM_WARN_MAP.remove(key);
            try {
                String title = ("进程上线告警恢复 " + appInfo.getHostname());
                String text = ("进程告警恢复" + appInfo.getHostname() + "\n\n "
                        + appInfo.getAppName() + "。");
                sendDing(title, text);
                WarnPools.MEM_WARN_MAP.put(key, "1");
                logInfoService.save(title, text, StaticKeys.LOG_ERROR);
            } catch (Exception e) {
                logger.error("发送进程恢复上线通知邮件失败：", e);
                logInfoService.save("发送进程恢复上线通知错误", e.toString(), StaticKeys.LOG_ERROR);
            }
        }
        return false;
    }

    public static boolean sendHeathInfo(HeathMonitor heathMonitor, boolean isDown) {
        String key = heathMonitor.getId();
        if (isDown) {
            if (!StringUtils.isEmpty(WarnPools.MEM_WARN_MAP.get(key))) {
                return false;
            }
            try {
                String title = "服务接口检测告警：" + heathMonitor.getAppName();
                String text = "**<font color=#750000 size=4>服务接口检测告警 Q2 </font>** \n\n " + getPhone()
                        + " 服务接口："
                        + heathMonitor.getHeathUrl() + "\n\n 响应状态码为" + heathMonitor.getHeathStatus()
                        + "  可能存在异常，请查看";
                //发送钉钉告警
                sendDing(title, text);
                //标记已发送过告警信息
                WarnPools.MEM_WARN_MAP.put(key, "1");
                //记录发送信息
                logInfoService.save(title, text, StaticKeys.LOG_ERROR);
            } catch (Exception e) {
                logger.error("发送服务健康检测告警信息失败：", e);
                logInfoService.save("发送服务健康检测告警信息错误", e.toString(), StaticKeys.LOG_ERROR);
            }
        } else {
            WarnPools.MEM_WARN_MAP.remove(key);
            try {
                String title = "服务接口恢复正常通知：" + heathMonitor.getAppName();
                String text = "服务接口恢复正常通知：" + heathMonitor.getHeathUrl() + "，响应状态码为"
                        + heathMonitor.getHeathStatus() + "";
                //发送钉钉信息
                sendDing(title, text);
                //记录发送信息
                logInfoService.save(title, text, StaticKeys.LOG_ERROR);
            } catch (Exception e) {
                logger.error("发送服务接口恢复正常通知信息失败：", e);
                logInfoService.save("发送服务接口恢复正常通知信息错误", e.toString(), StaticKeys.LOG_ERROR);
            }
        }
        return false;
    }

    /**
     * 网卡流量监测
     */
    public static boolean sendNetInfo(NetIoState netIoState) {
        if (netIoState.getRxbyt() != null && FormatUtil.formatDouble((Double.parseDouble(netIoState.getRxbyt())
                / 1024 / 1024) / netIoState.getNetSpeed() * 100,2) >= 75.0) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Double.parseDouble(netIoState.getRxbyt()) / 1024 / 1024 >= 90.0) {
                try {
                    String title = ("网卡告警");
                    String text = ("**<font color=#FF0000 size=6>网卡告警 Q1 </font>** \n\n " + getPhone()
                            + "\n\n发生的机器是:" + netIoState.getHostname() + " \n\n " + "当前网络流量为:"
                            + FormatUtil.formatDouble(Double.parseDouble(netIoState.getRxbyt()) / 1024 / 1024,
                            2) + " MB/s"
                            + "\n\n 事件发生的时间:" + date.format(netIoState.getCreateTime()));
                    sendDing(title, text);

                    logInfoService.save(title, text, StaticKeys.LOG_ERROR);
                } catch (Exception e) {
                    logger.error("发送网卡告警信息失败：", e);
                    logInfoService.save("发送网卡告警失败", e.toString(), StaticKeys.LOG_ERROR);
                }
            } else {
                try {
                    String title = ("网卡告警");
                    String text = ("**<font color=#750000 size=4>网卡告警 Q2 </font>** \n\n " + getPhone()
                            + "\n\n发生的机器是:" + netIoState.getHostname() + " \n\n " + "当前网络流量为:"
                            + FormatUtil.formatDouble(Double.parseDouble(netIoState.getRxbyt()) / 1024 / 1024
                            , 2) + " MB/s"
                            + "\n\n 事件发生的时间:" + date.format(netIoState.getCreateTime()));
                    sendDing(title, text);

                    logInfoService.save(title, text, StaticKeys.LOG_ERROR);
                } catch (Exception e) {
                    logger.error("发送网卡告警信息失败：", e);
                    logInfoService.save("发送网卡告警失败", e.toString(), StaticKeys.LOG_ERROR);
                }
            }
        }
        return false;
    }

    /**
     * 磁盘告警
     *
     * @param diskState
     * @return
     */
    public static boolean sendDiskinfo(JSONArray diskState) {
        /*
        获取到DISK使用总量并转换成number类型进行运算操作
         */
        Number DISKPer = 0;
        if (diskState != null) {
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            try {
                DISKPer = numberFormat.parse((String) ((JSONObject) diskState.get(0)).get("usePer"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ((double) DISKPer != 0 && (double) DISKPer >= 0.75) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if ((double) DISKPer >= 0.9) {
                try {
                    String title = ("磁盘告警");
                    String text = ("**<font color=#FF0000 size=6>磁盘告警 Q1 </font>** \n\n " + getPhone()
                            + "\n\n发生的机器是:" + ((JSONObject) diskState.get(0)).get("hostname") + " \n\n " + "当前磁盘容量为:"
                            + (double) DISKPer * 100 + "%"
                            + "\n\n 事件发生的时间:" + ((JSONObject) diskState.get(0)).get("createTime"));
                    sendDing(title, text);
                    logInfoService.save(title, text, StaticKeys.LOG_ERROR);
                } catch (Exception e) {
                    logger.error("发送磁盘告警信息失败：", e);
                    logInfoService.save("发送磁盘告警失败", e.toString(), StaticKeys.LOG_ERROR);
                }
            } else {
                try {
                    String title = ("磁盘告警");
                    String text = ("**<font color=#750000 size=6>磁盘告警 Q2 </font>** \n\n " + getPhone()
                            + "\n\n发生的机器是:" + ((JSONObject) diskState.get(0)).get("hostname") + " \n\n " + "当前磁盘容量为:"
                            + (double) DISKPer * 100 + "%"
                            + "\n\n 事件发生的时间:" + ((JSONObject) diskState.get(0)).get("createTime"));
                    sendDing(title, text);
                    logInfoService.save(title, text, StaticKeys.LOG_ERROR);
                } catch (Exception e) {
                    logger.error("发送磁盘告警信息失败：", e);
                    logInfoService.save("发送磁盘告警失败", e.toString(), StaticKeys.LOG_ERROR);
                }
            }
        }
        return false;
    }

    @Bean
    @Scheduled(initialDelay = 60000L, fixedRate = 60L * 10000)
    public void selectToPhone() {
        Map<String, Object> parase = new HashMap<>();
        DingSet ToPhones = null;
        try {
            List<DingSet> list = dingSetServer.selectByParams(parase);
            Phone = list.get(0).getToPhone();
            DingdingAPI = list.get(0).getFromDingName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    获取告警接收人手机号
    public static String iPhoneNum() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Phone.replace(";", "\n"));
        int x = 0;
        stringBuilder.insert(x += 0, "\"");
        try {
            for (int i = 0; i < (Phone.length() + 1) / 12; i++) {
                if (i == 0) {
                    stringBuilder.insert(x += 12, "\"");
                } else {
                    stringBuilder.insert(x += 2, "\"");
                    stringBuilder.insert(x += 12, "\"");
                }

            }
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("获取告警手机号失败", e);
            logInfoService.save("获取告警手机号失败", e.toString(), StaticKeys.LOG_ERROR);
        }

        return stringBuilder.toString();
    }

    //    钉钉告警@人
    public static String getPhone() {
        StringBuilder stringBuilder = new StringBuilder();
        ;
        stringBuilder.append(Phone.replace(";", "\t\t@"));
        stringBuilder.insert(0, "@");
        return stringBuilder.toString();

    }


    /*
    发送钉钉消息
     */
    public static String sendDing(String title, String text) {
//        消息模板
        String textmsg = "{ \"msgtype\": \"markdown\",\n" +
                "     \"markdown\": {\n" +
                "         \"title\": \"" + title + "\" ,\n" +
                "         \"text\": \"" + text + "\" \n" +
                "     },\n" +
                "      \"at\": {\n" +
                "          \"atMobiles\": [\n" +
                "               " + iPhoneNum() + "" +
//                "              \"15344062110\"\n" +
//                "                \"18248357502\"\n" +
                "          ],\n" +
                "          \"atUserIds\": [\n" +
                "               " + iPhoneNum() + "" +
//                "              \"15344062110\"\n" +
//                "                \"18248357502\"\n" +
                "          ],\n" +
                "          \"isAtAll\": false\n" +
                "      }}";
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("" + DingdingAPI + "");
            httpPost.setHeader("Content-type", "application/json;utf-8");
            StringEntity stringEntity = new StringEntity(textmsg, "utf-8");
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
//            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                System.out.println(EntityUtils.toString(httpResponse.getEntity(), "utf-8"));
//            }
            return "send success";
        } catch (Exception e) {
            logger.error("DingDing发送错误：", e);
            logInfoService.save("发送DingDing错误，", e.toString(), StaticKeys.LOG_ERROR);
            return "send error";
        }
    }

}

