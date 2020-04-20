package com.spas.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.Office;
import com.spas.backend.entity.User;
import com.spas.backend.mapper.OfficeMapper;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.util.PasswordHelper;
import com.spas.backend.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    userVo.setOfficeUrl(office.getUrl());
    userVo.setOfficeName(office.getName());
    return userVo;
  }

  @Override
  public UserDto selectUser(String email, String officeId) {
    return userMapper.selectUserByEmailAndOfficeId(email, officeId);
  }
}
