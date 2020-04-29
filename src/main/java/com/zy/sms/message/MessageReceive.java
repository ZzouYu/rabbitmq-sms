package com.zy.sms.message;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import com.zy.sms.utils.SmsUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author zouyu
 * @description
 * @date 2020/4/28
 */
@Component
@PropertySource("classpath:application-sms.properties")
public class MessageReceive {
    @Value("${smsCode}")
    private String smsCode;
    @Value("${param}")
    private String param;
    @Autowired
    private SmsUtil smsUtil;
    @RabbitListener(queuesToDeclare = @Queue("queueSms"))
    public void receiveMessage(Message message) throws UnsupportedEncodingException {
        String msg = new String(message.getBody(), "utf-8");
        Map<String,String> map = JSON.parseObject(msg, Map.class);
        String phone = map.get("phone");
        String code = map.get("code");
        String params = param.replace("[value]", code);
        try {
            CommonResponse commonResponse = smsUtil.sendSms(phone, smsCode, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(map.get("phone")+"----"+map.get("code"));
    }
}
