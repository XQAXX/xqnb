package com.dream.common.util.emali;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {
  int i=1;


    public static void main(String[] args) {
        SendEmail sendEmail=new SendEmail();
        sendEmail.send("455116383@qq.com");
    }
    /**
     * 用户注册发送邮件
     * @param email
     * @return
     */
   public  int send(String email){
        int number=(int)((Math.random()*9+1)*1000);
        // 收件人电子邮箱
        String to = email;
        // 发件人电子邮箱
        String from = "xqhlh@qq.com";
        // 指定发送邮件的主机为 smtp.qq.com
       //QQ 邮件服务器
        String host = "smtp.qq.com";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("xqhlh@qq.com", "isnivhzehxhgbiif"); //发件人邮件用户名、授权码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
      /*      message.setSubject("這是第"+i+"条");*/
            message.setSubject("你好！这里是众筹网站!");

            // 设置消息体
            message.setText("您的注册码是"+number+",欢迎来到本网站,有问题请联系我们的客户,谢谢合作!");
            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....from runoob.com");
            this.i++;
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return number;
    }
}