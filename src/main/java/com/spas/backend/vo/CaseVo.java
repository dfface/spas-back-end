package com.spas.backend.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CaseVo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String government;

  private String description;

  private String investigation;

  private String opinion;

  private Integer state;

  private LocalDateTime createTime;

  private LocalDateTime terminateTime;

  private LocalDateTime updateTime;

  private String creatorId;

  private String creatorName;
}
