package com.leyou.sms.untils;

import org.springframework.stereotype.Component;

@Component
public class SmsUtils {

   public  void sendSms(String phone,String code) {
       System.out.println("尊敬的"+phone+"手机号主"+"您的验证码为"+code);
   }

}