package com.spas.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.entity.Office;
import com.spas.backend.entity.Role;
import com.spas.backend.entity.User;
import com.spas.backend.service.OfficeService;
import com.spas.backend.service.RoleService;
import com.spas.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping("/offices")
public class OfficeController {

  @Resource
  private OfficeService officeService;

  @Resource
  private UserService userService;

  @Resource
  private RoleService roleService;

  @Value("${user.page.size}")
  private String pageSize;

  /**
   * 用于登录时，查询所有的检察院（必须存在，便于 Shiro 放过）.
   * @return 所有检察院列表
   */
  @GetMapping("/all")
  @ApiOperation("查询所有检察院，便于 Shiro 放行")
  public ApiResponse selectAll(){
    return new ApiResponse(officeService.list());
  }

  /**
   * 检察院管理，分页查询所有检察院信息.
   * @param current 当前页
   * @param size 每页大小
   * @return 所有检察院信息，以Page展示
   */
  @GetMapping("/{current}/{size}")
  @ApiOperation("检察院管理，分页查询所有检察院信息，与上一个方法雷同，故作修改")
  public ApiResponse selectAllByPage(@PathVariable long current, @PathVariable long size){
    long pageSizeGet;
    if(size < 0){
      pageSizeGet = size;
    }
    else{
      pageSizeGet = Integer.valueOf(pageSize).longValue();
    }
    IPage<Office> officeIPage = officeService.page(new Page<>(current, pageSizeGet));
    HashMap<String,Object> map = new HashMap<>();
    map.put("count",officeIPage.getPages());
    map.put("current",officeIPage.getCurrent());
    map.put("content",officeIPage.getRecords());
    return new ApiResponse(map);
  }

  /**
   * 检察院管理，编辑单个检察院信息.
   * @param office 检察院实体.
   * @return 成功与否
   */
  @PutMapping("")
  @ApiOperation("检察院管理，编辑单个检察院信息")
  public ApiResponse revise(@RequestBody Office office){
    return new ApiResponse(officeService.updateById(office));
  }

  /**
   * 检察院管理，新增检察院.
   * @param office 检察院实体.
   * @return 新检察院的id
   */
  @PostMapping("")
  @ApiOperation("检察院管理，新增检察院")
  public ApiResponse newOffice(@RequestBody Office office){
    ApiResponse apiResponse = new ApiResponse();
    officeService.save(office);
    apiResponse.setMsg(office.getId());
    apiResponse.setCode(ApiCode.OK.getIndex());
    // 新建的检察院的角色表都是固定的！因为就这么几个哈，还有啥，还有啥。操作表暂时没啥可用的，先放在这里。
    Role roleA = new Role();
    Role roleB = new Role();
    Role roleC = new Role();
    roleA.setCode("administrative_personnel");
    roleA.setDescription("行政机关人员");
    roleA.setOfficeId(office.getId());
    roleB.setCode("chief_procurator");
    roleB.setDescription("检察长");
    roleB.setOfficeId(office.getId());
    roleC.setCode("procurator");
    roleC.setDescription("检察官");
    roleC.setOfficeId(office.getId());
    List<Role> roleList = Arrays.asList(roleA, roleB, roleC);
    roleService.saveBatch(roleList);
    return apiResponse;
  }

  /**
   * 检察院管理，删除检察院.
   * @param id 检察院id
   * @return 成功与否
   */
  @DeleteMapping("/{id}")
  @ApiOperation("检察院管理，删除检察院")
  public ApiResponse delete(@PathVariable String id){
    Office office = officeService.getById(id);
    if(office != null){
      // 删除之前要慎重，看旗下还有没有用户，案件
      QueryWrapper<User> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("office_id",id);
      if(userService.list(queryWrapper) != null){
        // 无法删除，有用户
        return new ApiResponse(ApiCode.OFFICE_DELETE_FAILED);
      }
      else{
        // 可以删除
        officeService.removeById(id);
        return new ApiResponse();
      }
    }
    else{
      // 找不到检察院
      return new ApiResponse(ApiCode.OFFICE_NOT_FOUND);
    }
  }

}

