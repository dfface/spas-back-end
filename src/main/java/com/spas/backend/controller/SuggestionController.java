package com.spas.backend.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    // 保存
    Suggestion suggestion = new Suggestion();
    modelMapper.map(suggestionDto,suggestion);
    suggestionService.newSuggestion(suggestion);
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
      valueMap.put("secret",suggestionDto.getId());  // 密码应是检察建议的id
      valueMap.put("url","https://www.spas.com");  // 待定
      // 查询检察院名字
      Office office = officeService.select(suggestionDto.getOfficeId());
      valueMap.put("officeName",office.getName());
      valueMap.put("createTime", localDateTime.format(formatter));
      emailHelper.sendSimpleMail(valueMap,"suggestionSendTemplate");
    }
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
    // 查询关联的检察建议 且 检察建议 状态为1或者3
    QueryWrapper<SuggestionUser> suggestionUserQueryWrapper = new QueryWrapper<>();
    suggestionUserQueryWrapper.eq("use_id",userId)
        .eq("office_id",officeId);
    List<SuggestionUser> suggestionUserList = suggestionUserService.list(suggestionUserQueryWrapper);
    Assert.notNull(suggestionUserList);
    List<SuggestionVo> suggestionVoList = new ArrayList<>();
    for(SuggestionUser suggestionUser : suggestionUserList){
      Suggestion suggestion = suggestionService.select(suggestionUser.getSugId());
      if(suggestion.getState() == 1 || suggestion.getState() == 3){  // 1等待回复 2等待下一轮回复
        SuggestionVo suggestionVo = new SuggestionVo();
        modelMapper.map(suggestion,suggestionVo);
        suggestionVoList.add(suggestionVo);
      }
    }
    return new ApiResponse(ApiCode.OK,suggestionVoList);
  }

  /**
   * 检察官查看自己发送的检察建议需要处理的有哪些.
   * @param userId 检察官id
   * @param officeId 检察院id
   * @return List SuggestionVo
   */
  @GetMapping("/waitingReply/{userId}/{officeId}")
  @ApiOperation("检察官查看自己发送的检察建议需要处理的有哪些")
  public ApiResponse waitingReply(@PathVariable String userId, @PathVariable String officeId){
    if(userService.selectUserById(userId) == null){
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    if(officeService.getById(officeId) == null){
      return new ApiResponse(ApiCode.ILLEGAL_ARGUMENT);
    }
    // 查询自己发出的检察建议，且状态在1和3
    QueryWrapper<Suggestion> suggestionQueryWrapper = new QueryWrapper<>();
    suggestionQueryWrapper.eq("creator_id",userId)
        .eq("office_id",officeId)
        .eq("state",1)
        .or().eq("state",3)
        .or().eq("state",2);  // 等待评价
    List<Suggestion> suggestionList = suggestionService.list(suggestionQueryWrapper);
    List<SuggestionVo> suggestionVoList = new ArrayList<>();
    for (Suggestion suggestion : suggestionList) {
      SuggestionVo suggestionVo = new SuggestionVo();
      modelMapper.map(suggestion,suggestionVo);
      suggestionVoList.add(suggestionVo);
    }
    return new ApiResponse(ApiCode.OK,suggestionVoList);
  }

  /**
   * 行政单位人员，回复检察建议历史.
   * @param userId 用户id
   * @param current 当前页
   * @return OK, List SuggestionVo
   */
  @GetMapping("/replyHistory/{userId}/{current}")
  @ApiOperation("回复过的检察建议历史")
  public ApiResponse replyHistory(@PathVariable String userId, @PathVariable long current){
    QueryWrapper<SuggestionUser> suggestionUserQueryWrapper = new QueryWrapper<>();
    suggestionUserQueryWrapper.eq("use_id",userId);
    IPage<SuggestionUser> suggestionUserIPage = suggestionUserService.page(new Page<SuggestionUser>(current,Integer.parseInt(pageSize)),suggestionUserQueryWrapper);
    List<SuggestionUser> suggestionUserList = suggestionUserIPage.getRecords();
    List<SuggestionVo> suggestionVoList = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();
    map.put("count",suggestionUserIPage.getPages());
    map.put("current",suggestionUserIPage.getCurrent());
    for(SuggestionUser suggestionUser : suggestionUserList){
      Suggestion suggestion = suggestionService.getById(suggestionUser.getSugId());
      SuggestionVo suggestionVo = new SuggestionVo();
      modelMapper.map(suggestion,suggestionVo);
      suggestionVoList.add(suggestionVo);
    }
    map.put("content",suggestionVoList);
    return new ApiResponse(ApiCode.OK,map);
  }

}

