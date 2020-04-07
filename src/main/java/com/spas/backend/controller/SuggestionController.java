package com.spas.backend.controller;


import com.alibaba.fastjson.JSON;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.SuggestionDto;
import com.spas.backend.entity.Office;
import com.spas.backend.entity.Suggestion;
import com.spas.backend.service.OfficeService;
import com.spas.backend.service.SuggestionService;
import com.spas.backend.util.EmailHelper;
import com.spas.backend.vo.SuggestionVo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@RestController
@RequestMapping("/suggestion")
@Slf4j
public class SuggestionController {

  @Resource
  private SuggestionService suggestionService;

  @Resource
  private ModelMapper modelMapper;

  @Resource
  private EmailHelper emailHelper;

  @Resource
  private OfficeService officeService;

  @PostMapping("/new")
  public ApiResponse newSuggestion(@RequestBody SuggestionDto suggestionDto){
    // 参数校验
    // 发送邮件
    String[] email = suggestionDto.getEmail();
    log.info("email: " + Arrays.toString(email));
    if(email.length != 0){
      Map<String,Object> valueMap = new HashMap<>();
      LocalDateTime localDateTime = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      valueMap.put("to", email);
      valueMap.put("title","检察建议送达通知");
      valueMap.put("government",suggestionDto.getSupervisedName());
      valueMap.put("suggestion",suggestionDto.getContent());
      valueMap.put("deadline",suggestionDto.getDeadline().format(formatter));
      valueMap.put("secret",suggestionDto.getCaseId());
      valueMap.put("url","https://www.spas.com");  // 待定
      // 查询检察院名字
      Office office = officeService.select(suggestionDto.getOfficeId());
      valueMap.put("officeName",office.getName());
      valueMap.put("createTime", localDateTime.format(formatter));
      emailHelper.sendSimpleMail(valueMap,"suggestionSendTemplate");
    }
    // 保存
    Suggestion suggestion = new Suggestion();
    modelMapper.map(suggestionDto,suggestion);
    suggestionService.newSuggestion(suggestion);
    modelMapper.map(suggestion,suggestionDto);
    log.debug(suggestionDto.toString());
    return new ApiResponse(ApiCode.OK, suggestionDto);
  }

  @GetMapping("/detail/{id}")
  public ApiResponse detail(@PathVariable  String id){
    SuggestionVo suggestionVo = new SuggestionVo();
    modelMapper.map(suggestionService.select(id),suggestionVo);
    if(suggestionVo.getDeadline().isBefore(LocalDateTime.now())){
      suggestionVo.setSue(true);
    }
    return new ApiResponse(ApiCode.OK, suggestionVo);
  }
}

