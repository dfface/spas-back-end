package com.spas.backend;

import com.spas.backend.util.EmailHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MailTests {

  @Resource
  private EmailHelper emailHelper;

  @Test
  public void sendSuggestion() {
    Map<String,Object> valueMap = new HashMap<>();
    LocalDateTime localDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    valueMap.put("to", new String[]{"handy1998@qq.com"});
    valueMap.put("title","检察建议送达通知");
    valueMap.put("government","宜昌市水利水电局");
    valueMap.put("suggestion","你好，这是你的检察建议");
    valueMap.put("deadline","2020-06-05 23:38:04");
    valueMap.put("secret","7b84f9cc92fb26290efe9c60be54864e");
    valueMap.put("url","https://www.spas.com");
    valueMap.put("officeName","宜昌市人民检察院");
    valueMap.put("createTime", localDateTime.format(formatter));
    emailHelper.sendSimpleMail(valueMap,"suggestionSendTemplate");
  }
}
