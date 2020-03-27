package com.spas.backend.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class JsonUtil {

  static class HashMapStringString extends HashMap<String,String>{}

  public static HashMap<String,String> JsonToHashMap(String s){
    return JSON.parseObject(s,HashMapStringString.class);
  }
}
