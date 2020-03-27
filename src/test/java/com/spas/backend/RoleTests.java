package com.spas.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@SpringBootTest
public class RoleTests {
//  @Test
  public void test() {
    Set<String> set = new HashSet<>();
    set.add("hello");
    set.add("ni");
    System.out.println(set);
    System.out.println(Arrays.toString(set.toArray(new String[0])));
  }
}
