package com.wufei.springboot;


import com.wgcloud.config.DingTalk;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


public class Tests {


    @Test
    public void testDD() throws IOException {
//        Map<String,Object> map=new HashMap<>();
//        map.put("fff","fff");
//        System.out.println(map.toString());
//
//        JSONObject jsonObject = JSONObject.
//        System.out.println(jsonObject.toString());
        String tests = "{\n" +
                "     \"msgtype\": \"markdown\",\n" +
                "     \"markdown\": {\n" +
                "         \"title\":\"杭州天气\",\n" +
                "         \"text\": \"###杭州天气 @150XXXXXXXX \\n > <font face=\"逐浪新宋\">我是逐浪新宋</font> 9度，告警西北风1级，空气良89，相对温度73%\\n > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\\n > ###### 10点20分发布 [天气](https://www.dingtalk.com) \\n\"\n" +
                "     },\n" +
                "      \"at\": {\n" +
                "          \"atMobiles\": [\n" +
                "              \"150XXXXXXXX\"\n" +
                "          ],\n" +
                "          \"atUserIds\": [\n" +
                "              \"user123\"\n" +
                "          ],\n" +
                "          \"isAtAll\": false\n" +
                "      }\n" +
                " }";


        String url = "www.baidu.com";
//        String textmsg = "{ \"msgtype\": \"link\",\"link\": {\"title\":\"W1告警\",\"text\":\"告警测试\",\"picUrl\":\"https://cdn.pixabay.com/photo/2021/01/29/14/58/lake-5961239_960_720.jpg\",\"messageUrl\":\"http://123.57.158.15\"},\"at\":\"atMobiles\",\"atMobiles\":\"15344062110\",\"at\":\"isAtAll\",\"isAtAll\":\"false\"}";

//        String textmsg = "{ \"msgtype\": \"markdown\",\"markdown\": {\"title\":\"W1告警\",\"text\":\"告警测试,@15344062110\",\"at\":\"atMobiles\",\"atMobiles\":[\"15344062110\"],\"at\":\"atUserIds\",\"atUserIds\":[\"吴飞\"],\"at\":\"isAtAll\",\"isAtAll\":[\"false\"]}";
        String textmsg = "{ \"msgtype\": \"markdown\",\n" +
                "     \"markdown\": {\n" +
                "         \"title\":\"测试11\",\n" +
                "         \"text\": \" **<font color=#FF0000 size=6>红色-加粗 </font>** \n xx告警@15344062110, \n![](https://cdn.pixabay.com/photo/2021/01/29/14/58/lake-5961239_960_720.jpg)\n " +
                "                  [详情地址：] "+ url +"\" \n" +
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

        System.out.println(textmsg);
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://oapi.dingtalk.com/robot/send?access_token=e097866b2b35e774dcaf1087235ed986392bd4ecf42d1091bc1070063867dd95");
        httpPost.setHeader("Content-type", "application/json;utf-8");
        StringEntity stringEntity = new StringEntity(textmsg, "utf-8");
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            System.out.println(EntityUtils.toString(httpResponse.getEntity(), "utf-8"));
        }

    }

    @Autowired
    private DingTalk dingTalk;

    @Test
    public void showall(){
//
        boolean a = true;
        if (a){
            return;
        }
        System.out.println("111");








    }
}
