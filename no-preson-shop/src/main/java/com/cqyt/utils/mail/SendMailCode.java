package com.cqyt.utils.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮件授权码：ktgybhnoxtdsbbaf
 * Created by HP on 2020/2/22.
 */
@Component
@Slf4j
public class SendMailCode {
    //private String OtherMail;
    //private String number;
    //public SendMailCode(String OtherMail, String number){
    //    this.OtherMail=OtherMail;
    //    this.number=number;
    //}
    public void send(String OtherMail, String number) throws Exception {
        log.info("开始发送验证码");
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");//#设置qq邮箱服务器
        properties.setProperty("mail.transport.protocol", "smtp");//#邮件发送协议
        properties.setProperty("mail.smtp.auth", "true");//#需要验证用户名与密码
        //        QQ邮箱还需一下内容
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //1、创建Session
        //只有qq才有，其他邮箱没有
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人用户名和授权码
                return new PasswordAuthentication("2253586301@qq.com", "qsarsiqfvimgebbd");
            }
        });
        //开启debug，查看邮箱实时信息
//        session.setDebug(true);

        //2、通过Session得到transport,
        Transport transport = session.getTransport();
        //3、使用邮箱用户和授权码连接邮箱服务器
        transport.connect("smtp.qq.com", "2253586301@qq.com", "qsarsiqfvimgebbd");
        //4、创建邮箱
        //   MimeMessage 创建邮箱类容
        MimeMessage mimeMessage = new MimeMessage(session);
        //2253586301
        //指明发件人
        mimeMessage.setFrom(new InternetAddress("2253586301@qq.com"));
        //指明收件人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(OtherMail));
        //设置邮件主题
        mimeMessage.setSubject("校园淘注册验证码");
        //邮件的文本内容
        mimeMessage.setContent("你好，你当前的验证码是:"+number, "text/html;charset=utf-8");
        //5、发送邮件
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        log.info("验证码发送完毕");
        //关闭
        transport.close();
    }

}
