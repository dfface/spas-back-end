package com.spas.backend.service.impl;

import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.User;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.util.PasswordHelper;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Resource
  private UserMapper userMapper;

  private ModelMapper modelMapper;

  @Autowired
  private void setModelMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public ApiResponse insertUser(UserDto userDto) {
    Map<String,String> map = new PasswordHelper().hashPassword(userDto.getPassword());
    User user = new User();
    modelMapper.map(userDto, user);
    user.setPassword(map.get("password"));
    user.setSalt(map.get("salt"));
    userMapper.insert(user);
    return new ApiResponse(null);
  }

  @Override
  public ApiResponse selectUser(String email) {
    User user = userMapper.selectUserByEmail(email);
    UserDto userDto = new UserDto();
    modelMapper.map(user,userDto);
    return new ApiResponse(userDto);
  }
}
