package com.spas.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
@Slf4j
public class EmailHelper {

  @Resource
  private JavaMailSender javaMailSender;

  @Resource
  private TemplateEngine templateEngine;

  @Value("${spring.mail.username}")
  private String senderMailAddress;

  public void sendSimpleMail(Map<String, Object> valueMap, String mailTemplate){

    MimeMessage mimeMessage = null;

    try{
      mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);  // true 表示多部分，可添加内联资源
      // 设置邮件信息
      mimeMessageHelper.setFrom(senderMailAddress);
      mimeMessageHelper.setTo((String[]) valueMap.get("to"));
      mimeMessageHelper.setSubject(valueMap.get("title").toString());
      // 利用 Thymeleaf 引擎渲染 HTML
      Context context = new Context();
      context.setVariables(valueMap);  // 设置注入的变量
      String content = this.templateEngine.process(mailTemplate,context);  // 模板设置为 "mail"
      // 设置邮件内容
      mimeMessageHelper.setText(content,true); // true 表示开启 html
      javaMailSender.send(mimeMessage);
    }catch (MessagingException e){
      log.error("发送邮件出错：" + e.getMessage() + e.getCause());
    }
  }
}
