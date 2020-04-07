package com.spas.backend.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SuggestionVo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String supervisedName;

  private String content;

  private LocalDateTime deadline;

  private String caseId;

  private String creatorId;

  private String officeId;

  private Integer state;

  private LocalDateTime createTime;

  private Double score;

  private Boolean sue;
}
