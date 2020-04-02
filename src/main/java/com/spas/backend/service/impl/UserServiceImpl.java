package com.spas.backend.service.impl;

import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.Department;
import com.spas.backend.entity.Office;
import com.spas.backend.entity.User;
import com.spas.backend.mapper.DepartmentMapper;
import com.spas.backend.mapper.OfficeMapper;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.service.DepartmentService;
import com.spas.backend.service.OfficeService;
import com.spas.backend.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.util.PasswordHelper;
import com.spas.backend.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Resource
  private UserMapper userMapper;

  private ModelMapper modelMapper;

  @Resource
  private OfficeMapper officeMapper;

  @Resource
  private DepartmentMapper departmentMapper;

  @Autowired
  private void setModelMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public ApiResponse insertUser(UserDto userDto) {
    Map<String,String> map = new PasswordHelper().createPassword(userDto.getPassword());
    User user = new User();
    modelMapper.map(userDto, user);
    user.setPassword(map.get("password"));
    user.setSalt(map.get("salt"));
    userMapper.insert(user);
    return new ApiResponse();
  }

  @Override
  public UserDto selectUser(String email) {
    return userMapper.selectUserByEmail(email);
  }

  @Override
  public UserDto selectUserById(String id) {
    return userMapper.selectUserById(id);
  }

  @Override
  public UserVo selectUserByIdToVo(String id) {
    UserVo userVo = new UserVo();
    UserDto userDto = userMapper.selectUserById(id);
    modelMapper.map(userDto,userVo);
    Office office = officeMapper.selectById(userDto.getOfficeId());
    Department department = departmentMapper.selectById(userDto.getDepartmentId());
    userVo.setDepartmentName(department.getName());
    userVo.setOfficeUrl(office.getUrl());
    userVo.setOfficeEmail(office.getEmail());
    userVo.setOfficeName(office.getName());
    return userVo;
  }
}
