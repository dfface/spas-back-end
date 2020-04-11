package com.spas.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spas.backend.entity.SuggestionUser;

import java.util.List;

public interface SuggestionUserService extends IService<SuggestionUser> {

  List<SuggestionUser> selectSuggestionUser(String userId, String officeId);
}
