package com.spas.backend.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.SuggestionDto;
import com.spas.backend.entity.Office;
import com.spas.backend.entity.Suggestion;
import com.spas.backend.entity.SuggestionUser;
import com.spas.backend.service.OfficeService;
import com.spas.backend.service.SuggestionService;
import com.spas.backend.service.SuggestionUserService;
import com.spas.backend.service.UserService;
import com.spas.backend.util.EmailHelper;
import com.spas.backend.vo.SuggestionVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.Assert;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
  private UserService userService;

  @Resource
  private OfficeService officeService;


  @Resource
  private ModelMapper modelMapper;

  @Resource
  private SuggestionUserService suggestionUserService;

  @Resource
  private EmailHelper emailHelper;

  @Value("${user.page.size}")
  private String pageSize;

  @PostMapping("/new")
  @ApiOperation("新建检察建议")
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
  @ApiOperation("查看检察建议详细内容")
  public ApiResponse detail(@PathVariable  String id){
    SuggestionVo suggestionVo = new SuggestionVo();
    modelMapper.map(suggestionService.select(id),suggestionVo);
    if(suggestionVo.getDeadline().isBefore(LocalDateTime.now())){
      suggestionVo.setSue(true);
    }
    return new ApiResponse(ApiCode.OK, suggestionVo);
  }

  @GetMapping("/history/{id}/{current}")
  @ApiOperation("查看历史检察建议")
  public ApiResponse history(@PathVariable String id, @PathVariable long current){
    IPage<SuggestionVo> suggestionVos = suggestionService.selectSuggestionVoAllByPage(id,new Page<SuggestionVo>(current,Integer.valueOf(pageSize).longValue()));
    List<SuggestionVo> suggestionVoList = suggestionVos.getRecords();
    for(SuggestionVo suggestionVo : suggestionVoList){
      suggestionVo.setSue(suggestionVo.getDeadline().isBefore(LocalDateTime.now()));
    }
    Map<String,Object> map = new HashMap<>();
    map.put("count",suggestionVos.getPages());
    map.put("current",suggestionVos.getCurrent());
    map.put("content",suggestionVoList);
    return new ApiResponse(ApiCode.OK,map);
  }

  /**
   * 行政单位人员关联检察建议.
   * @param useId 用户id
   * @param sugId 检察建议id
   * @return OK
   */
  @PostMapping("/associate/{useId}/{sugId}/{officeId}")
  @ApiOperation("行政单位人员关联检察建议")
  public ApiResponse associate(@PathVariable String useId, @PathVariable String sugId, @PathVariable String officeId){
    // 参数校验
    // 先验证你是你
    if(userService.selectUserById(useId) == null){
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    if(suggestionService.getById(sugId) == null){
      return new ApiResponse(ApiCode.ILLEGAL_ARGUMENT);
    }
    if(officeService.getById(officeId) == null){
      return new ApiResponse(ApiCode.ILLEGAL_ARGUMENT);
    }
    SuggestionUser suggestionUser = new SuggestionUser();
    suggestionUser.setSugId(sugId);
    suggestionUser.setUseId(useId);
    suggestionUser.setOfficeId(officeId);
    suggestionUserService.save(suggestionUser);
    return new ApiResponse(ApiCode.OK);
  }

  /**
   * 正在回复处理中.
   * @param userId 用户id
   * @param officeId 检察院id
   * @return suggestionVo List
   */
  @GetMapping("/replying/{userId}/{officeId}")
  @ApiOperation("正在回复(处理中）的检察建议大纲列表")
  public ApiResponse replying(@PathVariable String userId, @PathVariable String officeId){
    if(userService.selectUserById(userId) == null){
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    if(officeService.getById(officeId) == null){
      return new ApiResponse(ApiCode.ILLEGAL_ARGUMENT);
    }
    // 查询关联的检察建议
    List<SuggestionUser> suggestionUserList = suggestionUserService.selectSuggestionUser(userId,officeId);
    Assert.notNull(suggestionUserList);
    List<SuggestionVo> suggestionVoList = new ArrayList<>();
    for(SuggestionUser suggestionUser : suggestionUserList){
      Suggestion suggestion = suggestionService.select(suggestionUser.getSugId());
      if(suggestion.getState() == 1){
        SuggestionVo suggestionVo = new SuggestionVo();
        modelMapper.map(suggestion,suggestionVo);
        suggestionVoList.add(suggestionVo);
      }
    }
    return new ApiResponse(ApiCode.OK,suggestionVoList);
  }

}

