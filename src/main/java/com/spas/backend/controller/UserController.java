package com.spas.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.common.exception.CustomException;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.Cases;
import com.spas.backend.entity.Office;
import com.spas.backend.entity.SuggestionUser;
import com.spas.backend.entity.User;
import com.spas.backend.service.CaseService;
import com.spas.backend.service.OfficeService;
import com.spas.backend.service.SuggestionUserService;
import com.spas.backend.service.UserService;
import com.spas.backend.util.JWTHelper;
import com.spas.backend.util.PasswordHelper;
import com.spas.backend.vo.UserOutlineVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

  @Resource
  private UserService userService;

  @Resource
  private OfficeService officeService;

  @Resource
  private CaseService caseService;

  @Resource
  private SuggestionUserService suggestionUserService;

  @Resource
  private RedisTemplate<String,String> redisTemplate;

  @Value("${user.page.size}")
  private String pageSize;

//  /**
//   * 添加用户.（在HomeController中有，这里不给予）
//   * @param userDto 用户详细信息
//   * @return 成功
//   */
//  @PostMapping("")
//  @ApiOperation("新注册用户")
//  public ApiResponse add(UserDto userDto){
//    return userService.insertUser(userDto);
//  }

//  @GetMapping("/{email}/")
//  @RequiresRoles("chief_procurator")
//  @ApiOperation("通过邮箱获取用户信息")
//  public ApiResponse get(@PathVariable String email) {
//    try {
//      return new ApiResponse(userService.selectUser(email));
//    } catch (Exception e) {
//      return new ApiResponse(ApiCode.ILLEGAL_ARGUMENT, "邮箱未注册！");
//    }
//  }

  /**
   * 用户管理，修改用户基本信息（除开密码、角色）.
   * @param userOutlineVo 用户简单信息
   */
  @PutMapping("")
  @ApiOperation("用户管理，修改用户基本信息（除开密码、角色）")
  @RequiresRoles(value = {"chief_procurator", "super_admin"}, logical = Logical.OR)
  public ApiResponse revise(@RequestBody UserOutlineVo userOutlineVo){
    userService.updateUserByUserOutlineVo(userOutlineVo);
    return new ApiResponse();
  }

  /**
   * 用户管理，通过officeId获取所有用户分页信息，不包含机密信息.
   * @param officeId 检察院id
   * @param current 当前页
   * @param size 页面大小
   * @return 分页的用户基本信息
   */
  @GetMapping("")
  @RequiresRoles(value = {"chief_procurator", "super_admin"}, logical = Logical.OR)
  public ApiResponse userInfo(@RequestParam String officeId, @RequestParam long current, @RequestParam long size){
    // 先查询 office 是否存在
    Office office = officeService.getById(officeId);
    if(office == null){
      return new ApiResponse(ApiCode.OFFICE_NOT_FOUND);
    }
    // 获取页面大小
    long pageSizeGet;
    if(size < 0){
      pageSizeGet = Integer.valueOf(pageSize).longValue();
    }
    else{
      pageSizeGet = size;
    }
    IPage<UserOutlineVo> userOutlineVoIPage = userService.selectUserOutlineVoByOfficeIdToPage(
       new Page<>(current,pageSizeGet),officeId
    );
    // 构造返回页面格式
    HashMap<String,Object> map = new HashMap<>();
    map.put("count",userOutlineVoIPage.getPages());
    map.put("current",userOutlineVoIPage.getCurrent());
    map.put("content",userOutlineVoIPage.getRecords());
    return new ApiResponse(map);
  }

  /**
   * 通过 id 删除用户.
   * @param id 用户id
   * @return 成功
   */
  @DeleteMapping("/{id}")
  @ApiOperation("通过 id 删除用户")
  @RequiresRoles(value = {"chief_procurator", "super_admin"}, logical = Logical.OR)
  public ApiResponse delete(@PathVariable String id){
    // 判断id是否正确
    User user = userService.getById(id);
    if(user == null){
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    // 删除检察官之前，还要查看用户是否有创建的案件
    QueryWrapper<Cases> casesQueryWrapper = new QueryWrapper<>();
    casesQueryWrapper.eq("creator_id",id);
    List<Cases> casesList = caseService.list(casesQueryWrapper);
    if(casesList != null){
      return new ApiResponse(ApiCode.USER_DELETE_FAILED);
    }
    // 删除行政单位人员之前，还要查看用户是否有关联的检察建议
    QueryWrapper<SuggestionUser> suggestionUserQueryWrapper = new QueryWrapper<>();
    suggestionUserQueryWrapper.eq("use_id",id);
    List<SuggestionUser> suggestionUserList = suggestionUserService.list(suggestionUserQueryWrapper);
    if(suggestionUserList != null){
      return new ApiResponse(ApiCode.USER_DELETE_FAILED);
    }
    // 一切都满足则可以删除
    userService.removeById(id);
    return new ApiResponse();
  }

  /**
   * 审核用户（激活/禁用）自己当然不能更改状态.
   * @param userId 用户id
   * @param state 状态
   * @return 成功
   */
  @PatchMapping("/{userId}/{state}")
  @ApiOperation("审核用户（激活/禁用）自己当然不能更改状态")
  @RequiresRoles(value = {"chief_procurator", "super_admin"}, logical = Logical.OR)
  public ApiResponse audit(@PathVariable String userId, @PathVariable int state){
    // 检查用户是否存在
    User user = userService.getById(userId);
    if(user == null){
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    // 检查状态合法性
    if(!ArrayUtils.contains(new int[]{1,2,3}, state)){
      return new ApiResponse(ApiCode.USER_STATE_FAILED);
    }
    // 更改用户状态
    user.setState(state);
    userService.updateById(user);
    return new ApiResponse();
  }

  /**
   * 重置用户密码，只能是管理员.
   * @param userId 用户id
   * @param password 新密码
   * @return 成功
   */
  @PatchMapping("/{userId}")
  @ApiOperation("重置用户密码，只能是管理员")
  @RequiresRoles(value = {"chief_procurator", "super_admin"}, logical = Logical.OR)
  public ApiResponse changePassword(@PathVariable String userId, @RequestBody String password){
    // 检查用户id的正确性
    User user = userService.getById(userId);
    if(user == null){
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    // 更新密码和盐值，如果 Redis 中有这个用户，让他下线
    ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
    if(valueOperations.get(userId) != null){
      valueOperations.set(userId,"quit",1); // 1微秒 强制下线
    }
    Map<String,String> map = new PasswordHelper().createPassword(password);
    user.setSalt(map.get("salt"));
    user.setPassword(map.get("password"));
    userService.updateById(user);
    return new ApiResponse();
  }

}

