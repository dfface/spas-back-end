package com.spas.backend.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaseOutlineVo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String description;

  private Integer state;
}
